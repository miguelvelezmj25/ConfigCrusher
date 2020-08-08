ConfigCrusher: Towards White-Box Performance Analysis for Configurable Systems
=========

This repo contains all material (implementations of ConfigCrusher and state-of-the-art black-box and white-box approaches, data, scripts, results, etc.) of our novel white-box performance analysis and empirical evaluation to state-of-the-art approaches.
This research shows the benefits and potential of our white-box analysis to efficiently generate performance models.

## Abstract

In configurable software systems, stakeholders are often interested in knowing how configuration options influence the performance of a system to facilitate, for example, the debugging and optimization processes of these systems.
There are several black-box approaches to obtain this information, but they either sample the system end-to-end with a large number of configurations to make accurate predictions or miss important performance-influencing interactions when sampling few configurations.
In addition, these approaches cannot pinpoint the parts of a program that are responsible for performance differences among configurations.
This paper proposes ConfigCrusher, a white-box performance analysis that inspects the implementation of a system to guide the performance analysis and exploits several insights about configurable systems in the process.
ConfigCrusher employs a static data-flow analysis to identify how configuration options may influence control-flow decisions and instruments code regions corresponding to these decisions to dynamically analyze the influence of configuration options on the regions' performance.
Our evaluation shows the feasibility of our white-box approach to more efficiently build performance models that are similar to or more accurate than current state-of-the-art approaches on 10 configurable systems.
Overall, we showcase the benefits of white-box performance analyses and their potential to outperform black-box approaches and provide additional information for analyzing configurable systems.

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
