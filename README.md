# What Does This Do?
This uses [BoofCV](http://boofcv.org) and [template matching](https://en.wikipedia.org/wiki/Template_matching) to tell if a larger image contains some smaller image

# How Do I Do It?
## As a Service
Download a version from [the release page](https://github.com/emanresusername/template-matching/releases), or assemble one yourself with `sbt assembly`.

Then to run the server on port `9999` you'd do:
```sh
/path/to/where/you/saved-or-assembled/template-matching-<version> 9999
```

It accepts json in the format

```json
{
"full":"/path/to/full/image",
"part":"/path/to/part/of/image"
}
```

and responds with `true` if `part` is contained in `full` and `false` otherwise

## Programmatically
```scala
import me.practechal.templatematching.ImageTester

ImageTester.doesImageContainPart("/path/to/full/image", "/path/to/part/of/image")
```

# Who Did It First?
The ImageTester class was adapted from [BoofCV's `ExampleTemplateMatching.java`](https://github.com/lessthanoptimal/BoofCV/blob/master/examples/src/boofcv/examples/features/ExampleTemplateMatching.java)
