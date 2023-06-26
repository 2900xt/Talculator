
--------------------------------------------------------------------------------------------------------------------------
# Basic use:

To graph a function, click on the plus icon at the bottom of the screen, or press insert key

To graph a regression line, click on the linear regression icon at the bottom of the screen or press alt + r

To clear graphs, press ctrl + r, or click on the reset button at bottom of the screen

To evaluate any function at a specific points, press ctrl + e or click on the evaluate button at the bottom of the screen

To zoom in, press ctrl and the plus key
To zoom out, press ctrl and the minus key

--------------------------------------------------------------------------------------------------------------------------

# Syntax for functions:

Polynomials, inverse polynomials, linear and constants are supported.
Example 1: f(x) = x^2 + 2 * x + 4
 - In this example, 'x^2' is x to the 2nd power, 2 * x is the first power and 4 is x to the zeroth power
 - Note: coefficients aren't supported, so you must use the * sign

 Example 2: f(x) = x^0.5
  - This is how to represent 'f(x) = sqrt(x)'

Factored form of polynomials and multiplication of equations are supported
Example 3: f(x) = (x + 4)(x + -4)
 - In this example, function 'f' is composed of two polynomial functions - 'x + 4' and 'x + -4' that are multiplied
 - Note: the entire function must be in factored form to be recognized as a factored form

Example 4: f(x) = (e^(x))(x^3)
 - In this example there is an exponential term that is being multiplied by a 3rd degree polynomial to make up 'f'

 Exponential and logarithmic functions are also supported
 Example 5: f(x) = e^(-3 * x)
  - This is the standard form for exponential functions

 Example 6: f(x) = ln(x)

Trig and inverse trig functions are also supported

Example 7: f(x) = cot(x) + -4
Example 8: f(x) = arccsc(4 * x) + 7

Inverse of functions are also supported, just prefix your functions with 'inv'
Example 9: f(x) = inv x^2
 - This turns into sqrt(x)

 Example 10: f(x) = inv e^(x)
 - This turns into ln(x)

 Derivatives of functions are also supported. Prefix the function with 'd/dx'
 Example 11: f(x) = d/dx x^3
 - This is 3 * x^2

Example 12: f(x) = d/dx sin(x)
- This is cos(x)
Negative terms aren't supported. Either multiply by -1 or just add the negative constant.
Coefficients aren't supported. Use the multiplication '*' symbol