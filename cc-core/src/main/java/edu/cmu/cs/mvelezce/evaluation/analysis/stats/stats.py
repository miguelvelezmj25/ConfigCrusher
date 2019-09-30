import math
import numpy as np
import scipy.stats


def mean_confidence_interval(data, confidence=0.95):
    a = 1.0 * np.array(data)

    # for i in a:
    #     print i

    n, min_max, mean, var, skew, kurt = scipy.stats.describe(a)
    std = math.sqrt(var)

    ci = scipy.stats.t.interval(confidence, n - 1, loc=mean, scale=std / math.sqrt(n))

    return mean, ci[0], ci[1]


def test_fifth_in_ci():
    yes = 0
    no = 0
    mu = 20
    sigma = 5
    samples = 10

    for i in range(2000):
        data = np.random.normal(mu, sigma, samples)
        mean, minci, maxci = mean_confidence_interval(data)
        fifth = np.random.normal(mu, sigma)

        if minci <= fifth <= maxci:
            yes += 1
        else:
            no += 1

    print "Yes: " + str(yes)
    print "No: " + str(no)


test_fifth_in_ci()
