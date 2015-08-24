Utilities
=========

Various java utility classes and common libraries.

TreePrinter
-----------

A class for printing hierarchical data. Make a `TreePrinter` object by specifying how to retrieve nodes and node children in the tree, then get the print string from the `TreePrinter`. This class is mostly useful for debugging purposes.

For example, if your tree contains hierarchical instances of `MyNode<T>`, the following code will print out the entire tree:

```java
MyNode<Object> myTree = new MyNode<>(/* Some data... */);

TreePrinter<MyNode<T>> tp = TreePrinter.of(n -> { return n.toString(); },
                                           n -> { return n.getChildren(); });

System.out.println(tp.getPrintString(myTree.getRoot()));
```

The output can be configured using the various `set_____` methods of the TreePrinter object.

Exceptions
----------

The `UnimplementedFeatureException` and `UnreachableCodeExceptions` are mostly for debuging.


Utility
-------

Some utility methods for operating on arrays, images, and bounded data.