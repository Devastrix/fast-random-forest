# What is FastRandomForest? #

FastRandomForest is a re-implementation of the [Random Forest](http://www.stat.berkeley.edu/~breiman/RandomForests/cc_home.htm) classifier (RF) for the [Weka](http://www.cs.waikato.ac.nz/ml/weka/) environment that brings speed and memory use improvements over the original Weka RF.

Speed gains depend on many factors, but a 2.5x increase over RF in Weka 3-7-10 is not uncommon.

For detailed tests of speed and classification accuracy, as well as description of optimizations in the code, please refer to the FastRandomForest wiki at http://code.google.com/p/fast-random-forest/w

or email the author at _fran.supek\AT\irb.hr_.

Unrelated to the FastRF project, an MPI-enabled version of the Random Forest algorithm written in Fortran 90 is available from http://parf.googlecode.com.

# License #

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the Free Software Foundation, Inc., 675 Mass
Ave, Cambridge, MA 02139, USA.


# Using from own Java code #

Just add FastRandomForest\_0.99.jar to your Java VM classpath by using the -cp
switch, or by changing project dependencies in NetBeans/Eclipse/whatever IDE
you use. Then use hr.irb.fastRandomForest.FastRandomForest as you would use any other
classifier, see instructions at the WekaWiki:

http://weka.wikispaces.com/Use+WEKA+in+your+Java+code


# Using from Weka Explorer or Experimenter (versions 3.7.2 or newer are supported) #

1. Add the FastRandomForest.jar to your Java classpath when starting Weka. This
is normally done by editing the line beginning with “cp=” in “RunWeka.ini”
If "cp=" doesn't exist, search for "cmd\_default=" and add after "#wekajar#;".

2. You need to extract the “GenericPropertiesCreator.props” file from your
weka.jar (jar files are ordinary zip archives, the
GenericPropertiesCreator.props is under /weka/gui).

3. Place the file you've just extracted into the directory where you have
installed Weka (on Windows this is commonly "C:\Program Files\Weka-3-7")

4. Under the
```
     # Lists the Classifiers-Packages I want to choose from
```
heading, add the line
```
     hr.irb.fastRandomForest
```
Do not forget to add a comma and a backslash to the previous line.

5. Use the “FastRandomForest” class is in the hr.irb.fastRandomForest
package in the "Classify" tab. The other two classes cannot be used directly.


