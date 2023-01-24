# the-mandelbrot-eye
A visualizer for the Mandelbrot Set fractal, made in Java.

## What is a fractal?
A fractal is a geometric shape containing detailed structure at arbitrarily small scales, i.e., it's an image you can zoom into forever and you'll keep finding details. These entitites are self-similar, meaning that, as you're zooming in you'll find the same structure you zoomed into!

![fractal example](https://media.tenor.com/62yG4csYM2QAAAAC/fractal.gif "Example of a fractal")

## [The Mandelbrot Set](https://en.wikipedia.org/wiki/Mandelbrot_set) 
Take the function f(z) = z^2 + c, and keep in mind that we0ll use it to follow the sequence of z1 = f(z0), z2 = f(z1)... you'll find that after each iteration this action can result in two phenomena:

* Convergence

  Selecting a constant **c** and a seed **x0** with the value of 0, and then evaluating those parameters in our function f(x) will always return numbers bound to a certain range, e.g.
  
  x0 = 0, x1 = 1, x2 = 0, x3 = -3...
  
* Divergence

Selecting a constant **c** and a seed **x0** with the value of 0, and then evaluating those parameters in our function f(x) will return numbers that increase towards infinity, e.g.
  
  x0 = 0, x1 = 4, x2 = 60, x3 = 2890, x4 = 679030...
  
Now, instead of using real numbers, let's use complex ones, so that our function f(x) now reads as

f(z) = z^2 + c

Let's take a complex plane that ranges from -2 to 2 in both the vertical and horizontal axis and make each coordinate a pixel, each one representing a complex number (being the real component the horizontal axis and the imaginary component the vertical one), and evaluate them in our function.

![Mandelbrot Set](https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Mandel_zoom_00_mandelbrot_set.jpg/800px-Mandel_zoom_00_mandelbrot_set.jpg "Magic! 🧠💥")

Those complex numbers that converge are those that belong to the Mandelbrot Set, and we can color each one of the pixels according to the number of iterations that they tolerate before diverging off to infinity: the black zones represent complexs numbers that remain bound, while others colored differently don't.

## How to use this visualizer?

Use the buttons to select the coordinates of the plane where you want to zoom in, and then select how much you want to get close, and watch your perspective of the set as it builds before your eyes!

I hope you enjoy this tool as much as I have, and can help you make your own implementation, since I found hard to get a clear and direct explanation of it when I was assigned this task in the Computer Graphics class.
