package mandelbrotSetDrawer;

public class MandelbrotIterator {

    /**
     * This class validates how much a complex number converges when iterated trough the Mandelbrot function
     * (whether a complex number belongs to the Mandelbrot set or not)
     */

    /** The maximum amount of iterations under the Mandelbrot function that a complex number can be
     * submmited to
     */
    private int maxIterations;

    /**
     * Iterator constructor
     * @param maxIterations the maximum amount of iterations under the Mandelbrot function that a complex number can be
     *                      submmited to
     */
    public MandelbrotIterator(int maxIterations){
        this.maxIterations = maxIterations;
    }

    /**
     * Returns how much a complex constant converges to the Mandelbrot function, given a complex seed.
     * @param c_real the real part of the constant
     * @param c_img the imaginary part of the constant
     * @param seed_real the real part of the seed
     * @param seed_img the imaginary part of the seed
     * @return the amount of iterations it takes to the complex constant to diverge
     */
    public int iterateComplex(double c_real, double c_img, double seed_real, double seed_img){

        // Seed will typically be 0
        double z_real = new Double(seed_real);
        double z_img = new Double(seed_img);
        // We manipulate the constant
        double c_r = new Double(c_real);
        double c_i = new Double(c_img);
        int iterations = 0;

        /* Calculate whether c(c real + c imaginary) belongs to the Mandelbrot set.
            If we reach the maximum number of iterations and ff the distance from the origin is greater than 2,
            (the radius in which we find the elements belonging to the set) the number converges (is part of the
            Mandelbrot Set), so we must exit the loop.

            The distance is calculated by Pithagoras (a^2 + b^2 = c^2).
        */
        while (((z_real * z_real + z_img * z_img) < 4 /* 2^2 */) && (iterations < maxIterations))
        {
            /* Calculate Mandelbrot function
                z = z*z + c, where z is a complex number

                As square of a complex number (a + ib)^2 = a^2 – b^2 + 2abi,
                where a^2 - b^2 is the real part and 2abi is imaginary part.
            */

            // Real part
            double aux_z_real = (z_real * z_real) - (z_img * z_img) + c_r;

            // Imaginary part
            z_img = (2 * z_real * z_img) + c_i;

            // Since both formulas require from each other, we must not alter their values before
            // we still need them.
            z_real = aux_z_real;

            // Increment count
            iterations++;
        }

        return iterations;
    }

    /**
     * Sets the maximum amount of iterations under the Mandelbrot function that a complex number
     * can be submmited to
     * @param maxIterations the maximum amount of iterations a complex number is iterated through the Mandelbrot function
     */
    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }
}
