

# VERSION 0.7 (first public release) #

The following changes were made over Weka's RandomForest implementation (in Weka
developer branch nightly snapshot of 31-Oct-2007).

The FastRfBagging class (replaces weka.classifiers.meta.Bagging):

  * The new method resampleFastRf has functionality similar to resampleWithWeights, with the following difference: When an instance is to be sampled multiple times, resampleWithWeights would add multiple copies of the instance to the newData; resampleFastRf increases the weight (by 1) for the copy already present. This reduces the number of instances by ~30%, speeding things up. The trees' splitting criteria should work equally on this kind of dataset because they explicitely take weights into account, however the forests produced are a bit different when this sampling procedure is used.  I'm not sure what exactly causes the difference, however the crossvalidation performance of the forest did not seem to deteriorate on my tests.
  * The FastRfBagging now sorts all instances on every attribute and stores this data into each bag's sortedIndices field, adjusting for instances absent from  the bag; this means the sort is now performed once per forest, and not once per RandomTree (as in ordinary RandomForest).

The FastRfInstances class (inherits from weka.core.Instances):
  * The resampleFastRf now generates 'bags' of type FastRfInstances, which differs from ordinary Instances only in having 2 additional fields: int[.md](.md)[.md](.md) sortedIndices and int[.md](.md) whatGoesWhere.

The FastRandomForest class (replaces weka.classifiers.trees.RandomForest):
  * This class differs from ordinary RandomForest basically only in using FastRfBagger with FastRfTree, instead of Bagger with RandomTree.

The FastRfTree class (replaces weka.classifiers.trees.RandomTree):
  * The FastRfTree has a different way of handling instances with missing values in attribute chosen for the split. The RandomTree used to 'split up' such an instance in two instances, with weights that are proportional to size of each branch at splitting point. Instead, FastRfTree assignes the instance to one of the branches at random, with bigger branches having a higher probability of getting the instance. This allows us to skip creating new arrays of instance weights and passing them down the tree, as the weights do not change anymore. My reasoning was that such an approximation was acceptable when a forest with lots (>100) of trees was created, although it may not be on a single tree.
  * The splitData procedure splits int[.md](.md)[.md](.md) sortedIndices into two (or more for categorical split attributes) int[.md](.md)[.md](.md)[.md](.md) subsetIndices. It was quite slow before because it checked whether an instance's split attribute value was above or below splitting threshold for each attribute in sortedIndices. Now, this checking is done only once, and results are stored into the dataset's whatGoesWhere array (dataset must be of type FastRfInstances).
  * As a consequence of the above, the exact branch sizes (even with instances having unknowns in the split attribute) are known in advance so subsetIndices arrays don't have to be 'resized' (i.e. a new shorter copy of each one created and the old one GCed – quite a lot of data being shuffled around).


# VERSION 0.71 #

The FastRfTree class:
During the normal training of the forest, a lot of instances of FastRfTree
are being created - one per split per tree. Since the trees in a Random
Forest normally contain n-1 splits each (for n instances), it could pay off
to make 'lean' versions of the FastRfTree that has less fields, and therefore
a smaller memory footprint.
  * m\_MotherForest (FastRandomForest) was introduced
  * m\_KValue (int) was removed; a call to getKValue looks the value up by asking the m\_MotherForest its KValue; setKValue was removed
  * ditto for m\_MaxDepth and getMaxDepth
  * ditto for m\_Info (no getter/setter)
  * m\_MinNum is now a static final field always = 1; it was never set to  another value before anyway
  * m\_RandomSeed and its getter and setter have been removed; instead, the FastRfBagging passes a random seed it has generated as a parameter of the BuildClassifier function - later, a Random object is generated from the seed and the seed is not used anymore. As a consequence of this, FastRfTree can't implement Randomizable anymore.
  * Some methods which are likely to go unused have been omitted: toGraph(), toString(), and leafString() - this doesn't help with size of trees in memory, I've done it just to clean up clutter.
  * The check whether the default classifier (ZeroR) should be built was moved to FastRandomForest's buildClassifier(); and check if it should be used in testing to FastRandomForest's distributionForInstance(). So there is no more need for m\_ZeroR field in FastRfTrees.

The Benchmark class was introduced, useful for quick comparisons (speed-wise
and accuracy-wise) to the weka.trees.RandomForest.


# VERSION 0.72 #

The FastRfTree class:
  * m\_Distribution field, and all references to it are removed; it was used only localy in buildTree() as a reference to another array. Also, in leaves m\_Distribution held (quite useless) copies of the m\_ClassProbs.
  * in buildTree(), local array classProbs was unnecesarily deep copied to m\_classProbs in every split; now it's only copied by reference, and only when a leaf is made. It's also normalized only in leaves (before, it was normalized in every node). To do:
    * last change sometimes causes an error, as the split function can produce an empty leaf (which it shouldn't!) and then there is no m\_classProbs of the parent leaf to look up - it wasn't saved!!


# VERSION 0.8 #

The FastRfInstances class was removed.

The DataCache class was introduced:
  * The DataCache stores a dataset that FastRandomTrees use for training. The data points are stored in a single-precision float array indexed by attribute first, and by instance second, allowing faster access by FastRandomTrees.
  * The DataCache can seed a random number generator from the stored data.
  * The DataCache creates bootstrap samples of data. This functionality was removed from FastRfBagging class.
    * There is a subtle change in the way sampling is performed; before, if an instance had higher weight, it was more likely to get sampled multiple times, and its weight in the bagged dataset would increase in multiples of 1 (i.e. 1.0, 2.0, 3.0...) Now, the sampling frequency is independant of instance original weight, and its weight in the sampled data increases in multiples of the original weight, for example 1.5, 3.0, 4.5... This ensures the bagging procedure conserves class proportions in the bagged data.
  * The DataCache also computes and stores the sorted order of the instances by any attribute. This functionality was removed from FastRfBagging class. Due to the way that the DataCache is organized, the sortedInstances arrays don't have to be readjusted in making bootstrap samples.

The FastRfUtils class was introduced:
  * Contains functions for sorting single-precision float arrays used in DataCache, and for normalization of double arrays.

The FastRfTree class was renamed to FastRandomTree.


# VERSION 0.9 #

The SplitCriteria class was introduced.
  * EntropyConditionedOnRows() and entropyOverColumns() functions are made a bit faster by avoiding normalization for total number of instances.
  * The fastLog2() function was introduced as a rough approximation of log base 2 logarithms by bitwise operations on the internal representation of floating point numbers. It is approx. 4 times faster than Math.log(), relieving a performance bottleneck of FastRandomTree.

Changes in the FastRandomTree class:
  * The value of splitting criterion before split (entropyOverColumns) is not re-calculated for each attribute (because it doesn't change between attributes) in the distribution(), but only once in buildTree()
  * Also, when searching for best split points, the entropyOverColumns is not subtracted from entropyConditionedOnRows() in every possible split point, only in the best one in the buildTree().
  * In the distribution() function:
    * before it was possible to create a split 'in the middle' of the first instance 0, which would result in empty nodes after the split and cause  errors after the (otherwise unnecessary) m\_classProbs was removed in 0.71. This 'empty node problem' is now fixed.
    * dist[.md](.md)[.md](.md) is now computed only after the split point has been found, and not updated continually by copying from currDist.
    * instance 0 is now skipped when looking for split points, as the split point 'before instance 0' is not sensible and entropy does not need to be computed.


# VERSION 0.95 #

Multithreading is used in classifier training, and in out-of-bag error
estimation.
  * The classifiers in FastRfBagging are trained in separate "tasks" which are  handled by an ExecutorService (the FixedThreadPool) which runs the tasks in more threads in parallel. If the number of threads is not specified, it will be set automatically to the available number of cores.
  * Estimating the out-of-bag (OOB) error is also multithreaded, using the newly introduced VotesCollector class
  * To support multithreading, the classes FastRandomTree and DataCache also had to be reworked. FastRandomTree now holds a field with a reference to its DataCache (set to null right after training), and the DataCache has its own reusableRandomGenerator; before, both of these objects were passed down the buildTree() recursion. Also, the FastRandomTree.run() method now replaces the initial call of buildTree().

Two rarely occuring bugs were fixed in finding split points.
  * Sometimes an attribute with all missing values could be judged as carrying some information after split, and selected as the split criterion. while the splitpoint would be set to -Double.MAX\_VALUE. This would result in unnecessary (but otherwise harmless) additional branches in the tree.
  * Due to imprecision of the fastLog2 function, some splits which would normally have reduction of entropy of 0 (ie. be useless) actually had a very small positive value; this was fixed by checking reduction of entropy against a small constant (0.01) instead of against 0.

Serialization is no longer used to make copies of the initial FastRandomTree.
  * It's much faster to create the trees using the normal constructor and fill their fields with necessary data. The whole FastRandomForest may normally be  serialized, as before.


# VERSION 0.97 #

Changes in normalization of class probabilities in leaves (m\_ClassProbs)
  * In Weka RF, the final m\_ClassProbs was obtained by normalizing the sums of  per-class instance weights so that their total equals to 1.0; now, they are normalized by dividing sum of instance weights in each class with the total number of instances in node. This subtle change has two implications:
    1. As a consequence of bootstrap sampling, instances get varying weights if they are sampled multiple times. Now we allow this to influence how trees vote, which decorrelates individual trees and may have a mild positive effect on predictive ability of the forest.
      1. More importantly, if user changes instance weights before training  (for example, to put more emphasis on a rare class), the current normalization scheme will cause this change to shift the per-class probabilities (prediction) supplied by the forest. Before this wasn't the case.
  * In parallel with the above, the manner in which the out-of-bag (OOB) error estimate is computed has been changed. OOB estimation in Weka's Bagging is one tree - one vote. In FastRF 0.97 onwards, some trees will have a heavier weight in the overall vote depending on the averaged weights of instances that ended in the specific leaf. Also, if impure nodes are present, trees may vote for more than one class simultaneously.


# VERSION 0.98 #

Change in handling empty nodes.
  * Empty nodes frequently appear when splitting on nominal attributes with more than two categories. What happens if an instance ends up in an empty node during testing?
    * In Weka RF, the class distribution of its parent node would be looked up and offered as an answer.
    * In FastRF prior to version 0.98, the tree would vote with all zeroes, effectively excluding its vote from the forest when testing the given instance.
    * To increase compatibility, FastRF 0.98 onwards reverts to a behavior similar to Weka RF. However, the class distributions of the non-leaf nodes are not saved, and therefore we have to check whether a node will be empty **before** performing the split and set its class probabilities in the parent's buildTree() call.

Memory use optimizations in the DataCache.
  * The creation of the whatGoesWhere[.md](.md) for each bag is delayed; this array is now created in the FastRandomTree.run().
  * The creation of the sortedIndices[.md](.md)[.md](.md) for each bag is delayed; it was created in DataCache.resample(), but now it is created in the FastRandomTree.run().

Changes to the Benchmark class.
  * Switched to a generalization of AUC score to multiclass problems as described in Provost and Domingos (CeDER Working Paper #IS-00-04, Stern School of Business, New York University, 2001), computed by taking a **weighted** average over all one-vs-all binary classification problems that can be derived from the multiclass problem, where weights correspond to class prior probabilities.
  * System.gc() is invoked after each classifier.


# VERSION 0.99 #

Multiple speed and memory optimizations, for more details, please refer to javadoc of the following:
  * in the class FastRandomTree, please see function buildTree(), and the new functions splitDataNew() and distributionSequentialAtt()
  * in the FastRfBagging, see function computeOOBError( DataCache data, ... ) and its helper class VotesCollectorDataCache, and also the buildClassifier() where the feature importances are computed

Nominal attributes with more than 2 levels are treated differently than before; in 0.98 and previous, as well as Weka's Random Forest, for such attributes, splits with many branches are created. In 0.99, all splits are binary, and the best value of the nominal attribute (in a one-vs-rest fashion) is chosen for the split. Thus, the accuracy of 0.99 in datasets with multilevel nominal attributes may differ somewhat (in either direction) from Weka's standard implementation.

The feature to compute RF attribute importances is accessible via the Explorer GUI and is off by default, as it incurs a significant computational cost. Results of the feature importance calculation are now displayed in the GUI; they are output by the FastRandomForest toString() function.

Small changes in the output format of the Benchmark class.