import numpy as np
import scipy as sp
import scipy.stats


def mean_confidence_interval(data, confidence=0.95):
    a = 1.0 * np.array(data)
    n = len(a)
    m, se = np.mean(a), scipy.stats.sem(a)
    ci = sp.stats.t._ppf((1+confidence)/2., n-1)
    h = se * ci
    return m, m-h, m+h


def test_fifth_in_ci():
    yes = 0
    no = 0
    mu = 20
    sigma = 1

    for i in range(10000):
        data = np.random.normal(mu, sigma, 4)
        mean, minci, maxci = mean_confidence_interval(data)
        fifth = np.random.normal(mu, sigma)

        if minci <= fifth <= maxci:
            yes += 1
        else:
            no += 1

    print "Yes: " + str(yes)
    print "No: " + str(no)


test_fifth_in_ci()
