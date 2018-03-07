ConfigCrusher: White-Box Performance Analysis for Configurable Systems
=========

![](https://github.com/miguelvelezmj25/papers/blob/master/configcrusher/figures/concept/error.pdf)

## Abstract

In modern configurable systems, we are often interested in knowing how configuration options influence the performance of the system.
Several approaches exist to obtain this information, but they usually require a large number of samples to make accurate predictions and some impose limitations on the systems that they can analyze.
This paper proposes ConfigCrusher, a new white-box performance analysis approach for configurable systems.
ConfigCrusher combines static taint analysis and dynamic analysis to identify how configuration options may influence control-flow decisions (considering control-flow and data-flow dependencies) and instruments code regions corresponding to these decisions to measure the influence of options on the regions’ performance.
Our evaluation using 10 real-world configurable systems shows that ConfigCrusher is more efficient at building performance models that are similar or more accurate than current state-of-the-art black-box and white-box approaches.
To this end, we discuss the benefits and potential of our white-box performance analysis to outperform other approaches.