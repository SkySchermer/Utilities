Utilities
=========

Various java utility classes and common libraries.

Color
-----

### Color
The `Color` class provides a number of useful color manipulating functions, namely a large suite of flexable constructors and component manipulators. This allows ease of use when working with colors in multiple representations: RGB, CMYK, HSL, and HSV. It also provides some interaperablility with `java.awt.Color` and `Vector3f` (see below.)

```java
ColorNameSource cns = ColorNameSource.fromFile(DEFAULT_COLOR_NAME_FILE, Color.RGB_DISTANCE_FUNCTION);
        
Color c = Color.fromRgbHex(0x123456).dropChannel(Channel.BLUE).lighten(0.6f);

System.out.println(cns.getNearestColorName(c)); // prints "FRENCH LIME"
System.out.println(cns.getNearestColor(c)); // prints "#9EFD38"
```

### Color Sorting

`Color` also provides limited sorting capability in the form of `Color.COLOR_COMPARATOR`, which will sort colors by lightness, saturation, and hue. Due to the fact that colors are normally unordered, this will result in some 'banding' that makes it hard to relate colors that are dissimilar. This is mostly useful for displaying a large number of colors consistently in a palette.

### Transform

Colors can also be created using arbitrary matrix transformations. For instance, 
one could make a transformation that mixes the red and blue channels by doing this:

```java
Color c = Color.fromRgbHex(0x1134FF).transform(Matrix3f.of(0.5f, 0, 0.5f, 
                                                           0,    1, 0, 
                                                           0.5f, 0, 0.5f));
System.out.println(c) // prints "#883488" (~Plum)
```

This is useful for doing complicated gamma corrections. There is also a default SRGB gamma correction transformation in `Color.SRGB_GAMMA_CORRECTION`. (For instance, this is used for doing color comparisons based on visual similarity.)

```java
Color c = Color.fromRgbHex(0x1134FF).transform(Color.SRGB_GAMMA_CORRECTION);
System.out.println(c) // prints "#032512" (~Dark Green)
```


### ColorNameSource
The `ColorNameSource` class allows you to generate and find colors based on strings. This is useful for shortcutting color creation:

```java
System.out.println(Color.named("ochre")); // prints "#CC7722"
System.out.println(Color.named("medium vermilion")); // prints "#D9603B"
```

These functions are built into the `Color` class. Using `ColorNameSource` directly, you can do the same:

```java
ColorNameSource cns = ColorNameSource.fromFile("colornames.txt");
System.out.println(cns.getColor("dark magenta")); // prints "#8B008B"
```


You can also do nearest neighbor searches:

```java
System.out.println(cns.getNearestColorName(Color.fromRgbHex(0x123456)));
    // prints "ZINNWALDITE BROWN"

System.out.println(cns.getNearestColor(Color.fromRgbHex(0x123456)));
    // prints "#2C1608"
```

You can choose which metric to use for your color space when creating the `ColorNameSource`. The default matric is `Color.HSL_DISTANCE_FUNCTION`, but `Color.RGB_DISTANCE_FUNCTION` is also available. (You could write your own, of course.)