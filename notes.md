# NOTES #

src/lib/RectangularTiler.java

If the division is a partial fraction then we discard some pixels.

Example:

Picture dimension: 379*400

Case 1) 4 columns and 4 rows:

379/4=94.75 and 400/4=100

So, we have 4*4=16 blocks of 94*100 pixels.
We have lost 94*4=376 -> 379-376=3 pixels of the original picture.

Case 2) 53*100 pixel blocks

379/53=7.15 and 400/100=4

So, we have 7*4=28 blocks of 53*100 pixels.
We have lost 7*53=371 -> 379-371=8 pixels of the original picture.


Solution:

We find the best block size that discards the less proportion of image.
We can get the desirable block dimensions or columns-rows number(= number of blocks) 
and find the next best value. 
