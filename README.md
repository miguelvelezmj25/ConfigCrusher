ConfigCrusher: White-Box Performance Analysis for Configurable Systems
=========

This repo contains all material (implementations of ConfigCrusher and state-of-the-art black-box and white-box approaches, data, scripts, results, etc.) of our novel white-box performance analysis and empirical evaluation to state-of-the-art approaches.
This research shows the benefits and potential of our white-box analysis to efficiently generate performance models.

## Abstract

In modern configurable systems, we are often interested in knowing how configuration options influence the performance of the system.
Several approaches exist to obtain this information, but they usually require a large number of samples to make accurate predictions and some impose limitations on the systems that they can analyze.
This paper proposes ConfigCrusher, a new white-box performance analysis approach for configurable systems.
ConfigCrusher combines static taint analysis and dynamic analysis to identify how configuration options may influence control-flow decisions (considering control-flow and data-flow dependencies) and instruments code regions corresponding to these decisions to measure the influence of options on the regions’ performance.
Our evaluation using 10 real-world configurable systems shows that ConfigCrusher is more efficient at building performance models that are similar or more accurate than current state-of-the-art black-box and white-box approaches.
To this end, we discuss the benefits and potential of our white-box performance analysis to outperform other approaches.

## Supplementary material

[Link](https://github.com/miguelvelezmj25/ConfigCrusher/blob/supplementary/src/main/resources/supplementary)

## License

MIT License

Copyright (c) 2018 Miguel Velez

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.