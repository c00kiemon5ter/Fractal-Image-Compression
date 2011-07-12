Usage Scenarios
===============

Usually, images are expensive, especially when the quality is greater than needed.
Compression is, in almost every case, necessary.
Databases holding thousands or millions of images, or network data transfers,
can benefit from different compression schemes, in terms of less storage space needed
and less data to transfer over the wire, less bandwidth consumption
and thus more efficient and faster browsing and less cost and charges by web hosting services.

Lossy compression schemes, such as fractal compression, can be used for images,
that need not great detail and quality, like landscapes, but not medical images.
Low bandwidth and few resource systems can benefit by lossy compression schemes,
as they process less data, whereas big volume data would result to delays and
greater chance or increased error rates or even deformation of data.

**&raquo; Fractal compression features**

* _Asymmetric Compression_:
  Encoding is extremely computationally expensive because of the search used to
  find the self-similarities. Decoding, however is quite fast.
* _Resolution Indepedence_:
  An inherent feature of fractal compression is that images become resolution
  independent after being converted to fractal code. A fractal compressed image
  can be decompressed to any size, without loss in its quality.
* _Interpolation_:
  The resolution independence of a fractal-encoded image can be used to increase
  the display resolution of an image. This process is also known as "fractal
  interpolation". Fractal interpolation maintains geometric detail very well
  compared to traditional interpolation methods like bilinear interpolation
  and bicubic interpolation.

