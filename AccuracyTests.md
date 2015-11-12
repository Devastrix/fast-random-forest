# Summary #

I have downloaded a set of arff files with UCI repository datasets from http://www.cs.waikato.ac.nz/~ml/weka/index_datasets.html, and used the 17 largest datasets (>500 instances) from the group. Then, the FastRF's built-in Benchmark tool to compare classification accuracy of the original Weka RF (ver. 3-7-10) to FastRandomForest 0.99 on the 17 datasets.

Ten runs of tenfold crossvalidation were used. I've set the forest size to 100 trees. The Benchmark tool uses Weka's Corrected Paired t-Tester to determine winners in
  * classification accuracy, as % instances classified correctly
  * a weighted average of per-class AUC scores; weights derive from class sizes (after: _Provost and Domingos, CeDER Working Paper #IS-00-04, Stern School of Business, New York University, 2001_)

In brief, Weka RF was better by either accuracy or area-under-ROC in 6 of 17 datasets, while FastRandomForest 0.99 was better in 4 of 17 datasets. Overall the differences are quite small. In one dataset (splice.arff) the AUC and accuracy favored FastRF and WekaRF, respectively.

# Overview by dataset #

| **dataset** | **# Instances** | **# NumericAtt** | **# NominalAtt** | **# Classes** | **Weka AUC** | **FastRF AUC** | **AUC diff?** | **Weka accuracy (%)** | **FastRF accuracy (%)** | **accuracy diff?** |
|:------------|:----------------|:-----------------|:-----------------|:--------------|:-------------|:---------------|:--------------|:----------------------|:------------------------|:-------------------|
| anneal.arff | 898             | 6                | 32               | 6             | 1 +- 0.001   | 0.998 +- 0.002 | p=0.0067 (WekaRF wins) | 99.6 +- 0.1           | 99.3 +- 0.1             | p=0.0004 (WekaRF wins) |
| balance-scale.arff | 625             | 4                | 0                | 3             | 0.946 +- 0.001 | 0.945 +- 0.001 | p=0.7607 (no win) | 81.6 +- 0.4           | 81.3 +- 0.2             | p=0.3351 (no win)  |
| breast-w.arff | 699             | 9                | 0                | 2             | 0.989 +- 0.001 | 0.991 +- 0.001 | p=0.0093 (FastRF wins) | 96.7 +- 0.3           | 96.8 +- 0.3             | p=0.6867 (no win)  |
| credit-a.arff | 690             | 6                | 9                | 2             | 0.924 +- 0.003 | 0.93 +- 0.003  | p=0.0053 (FastRF wins) | 86.2 +- 0.6           | 86.5 +- 0.7             | p=0.5793 (no win)  |
| credit-g.arff | 1000            | 7                | 13               | 2             | 0.778 +- 0.007 | 0.76 +- 0.005  | p=0.0028 (WekaRF wins) | 75.8 +- 0.5           | 75.1 +- 0.5             | p=0.0374 (WekaRF wins) |
| diabetes.arff | 768             | 8                | 0                | 2             | 0.825 +- 0.006 | 0.825 +- 0.005 | p=0.3968 (no win) | 76.3 +- 0.9           | 76.2 +- 0.6             | p=0.8826 (no win)  |
| hypothyroid.arff | 3772            | 7                | 22               | 4             | 0.999 +- 0   | 0.999 +- 0     | p=0.0623 (no win) | 99.4 +- 0.1           | 99.5 +- 0               | p=0.0043 (FastRF wins) |
| kr-vs-kp.arff | 3196            | 0                | 36               | 2             | 0.999 +- 0   | 0.999 +- 0     | p=0.0009 (WekaRF wins) | 99.2 +- 0.1           | 98.7 +- 0.1             | p=0.0001 (WekaRF wins) |
| mushroom.arff | 8124            | 0                | 22               | 2             | 1 +- 0       | 1 +- 0         | p=1.0000 (no win) | 100 +- 0              | 100 +- 0                | p=1.0000 (no win)  |
| segment.arff | 2310            | 19               | 0                | 7             | 0.999 +- 0   | 0.999 +- 0     | p=0.3667 (no win) | 98.1 +- 0.2           | 98 +- 0.1               | p=0.1842 (no win)  |
| sick.arff   | 3772            | 7                | 22               | 2             | 0.997 +- 0   | 0.996 +- 0.001 | p=0.0053 (WekaRF wins) | 98.3 +- 0.1           | 98.4 +- 0.1             | p=0.3201 (no win)  |
| soybean.arff | 683             | 0                | 35               | 19            | 0.996 +- 0   | 0.994 +- 0.001 | p=0.0004 (WekaRF wins) | 93.2 +- 0.5           | 92.5 +- 0.4             | p=0.0216 (WekaRF wins) |
| vehicle.arff | 846             | 18               | 0                | 4             | 0.93 +- 0.002 | 0.93 +- 0.003  | p=0.6856 (no win) | 74.7 +- 0.8           | 75.2 +- 0.8             | p=0.2982 (no win)  |
| vowel.arff  | 990             | 10               | 3                | 11            | 1 +- 0       | 0.999 +- 0     | p=0.0000 (WekaRF wins) | 98.1 +- 0.2           | 96.8 +- 0.4             | p=0.0001 (WekaRF wins) |
| letter.arff | 20000           | 16               | 0                | 26            | 0.999 +- 0   | 0.999 +- 0     | p=0.0167 (FastRF wins) | 96.2 +- 0.1           | 96.2 +- 0.1             | p=0.1072 (no win)  |
| splice.arff | 3190            | 0                | 61               | 3             | 0.993 +- 0.001 | 0.99 +- 0      | p=0.0001 (WekaRF wins) | 93 +- 0.4             | 94.1 +- 0.3             | p=0.0026 (FastRF wins) |
| waveform-5000.arff | 5000            | 40               | 0                | 3             | 0.965 +- 0   | 0.965 +- 0.001 | p=0.4064 (no win) | 84.8 +- 0.3           | 84.8 +- 0.2             | p=0.9817 (no win)  |

.