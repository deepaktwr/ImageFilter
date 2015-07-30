# ImageFilter

Images has been filtered using shaders of opengl, multiple shaders has been present as a part of simple demos too.

# Technical Requirements

OpenGl Es 3.00

Java 1.7 onwards

Android api level 19

GLSL version 300 es

opengl 3.00 can't run on emulators, so it may need a real device.

no native support of NDK has been integrated yet, it may be found on next commits for performence benifits and use of
C/C++ libraries.

# Fuctionality

Two fragments has been present, one for demos and another for filtering images.
the demos has not been sufficient yet as it need some C++ libraries which will be integrated with NDK in next commits.

the Renderers are different for demos and Filters, while the GL SurfaceView is same.

the FragmentHelper do not have back stack management, will be integrated in next commits.

# References

http://alfonse.bitbucket.org/oldtut/

https://www.khronos.org/opengles/sdk/docs/man3/
https://www.khronos.org/registry/gles/specs/3.0/GLSL_ES_Specification_3.00.3.pdf
