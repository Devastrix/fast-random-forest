# Setup and Summary #

I've downloaded a set of UCI repository datasets from http://www.cs.waikato.ac.nz/~ml/weka/index_datasets.html, and tested on 17 of the datasets with >500 instances. A single run of tenfold crossvalidation was used, and number of threads set to 8 for both the Weka Random Forest and the FastRandomForest 0.99. Java version is 1.7.0 (64 bit), heap size 2 GB, 64-bit Windows 7, Core i7-2630QM (2 GHz, four cores w/hyperthreading).

In brief, you can expect (on average) a 2.3x speedup over Weka's original RF. This depends on the dataset -  with a bigger increase in datasets that are larger and that have fewer categorical attributes - and also on the underlying hardware.

(note: This comparison was run on Weka 3-7-0 and is valid for Weka 3-6-1 and newer; older versions of Weka had a much slower RF implementation.)



# Speed tests #

I've used the FastRF's built-in Benchmark tool to compare the speed of the original Weka RF (ver. 3-7-10) to FastRandomForest 0.99 on 17 datasets. The forest size was set to 1000 trees (except where stated differently - three large datasets at bottom), and one run of 10-fold crossvalidation was used.

Average speed-up factor: FastRF is **2.3 x** faster.

| **dataset** | **# instances** | **# numeric atts** | **# nominal attrs**| **num classes** | **speedup** |
|:------------|:----------------|:-------------------|:-------------------|:----------------|:------------|
| anneal      | 898             | 6                  | 32                 | 6               | 1.42 x      |
| balance-scale | 625             | 4                  | 0                  | 3               | 3.14 x      |
| breast-w    | 699             | 9                  | 0                  | 2               | 3.45 x      |
| credit-a    | 690             | 6                  | 9                  | 2               | 2.25 x      |
| credit-g    | 1000            | 7                  | 13                 | 2               | 1.20 x      |
| diabetes    | 768             | 8                  | 0                  | 2               | 2.78 x      |
| hypothyroid | 3772            | 7                  | 22                 | 4               | 2.42 x      |
| kr-vs-kp    | 3196            | 0                  | 36                 | 2               | 1.27 x      |
| mushroom    | 8124            | 0                  | 22                 | 2               | 1.20 x      |
| segment     | 2310            | 19                 | 0                  | 7               | 2.65 x      |
| sick        | 3772            | 7                  | 22                 | 2               | 2.11 x      |
| soybean     | 683             | 0                  | 35                 | 19              | 2.24 x      |
| vehicle     | 846             | 18                 | 0                  | 4               | 2.58 x      |
| vowel       | 990             | 10                 | 3                  | 11              | 1.62 x      |
| waveform-5000 (100 trees) | 5000            | 40                 | 0                  | 3               | 2.60 x      |
| letter (100 trees) | 20000           | 16                 | 0                  | 26              | 3.10 x      |
| splice (100 trees) | 3190            | 0                  | 61                 | 3               | 3.46 x      |

.