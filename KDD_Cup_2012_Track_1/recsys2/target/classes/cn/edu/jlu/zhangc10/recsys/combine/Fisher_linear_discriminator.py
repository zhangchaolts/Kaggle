
from numpy import *

import sys,string

def cal_m(data,gender):
    fin = open(data, 'r')
    m = mat([[0],[0],[0]])
    total = 0;
    ptr = 1
    while 1:
        ptr = ptr + 1
        total = total + 1
        line = fin.readline()
        if not line:
            break
        term = line.split()
        if(term[0] == gender):
            temp = mat([[float(term[1])],[float(term[2])],[float(term[3])]])
            m = m + temp
    m = m / float(total)
    return m

def cal_s(data,gender,m):
    fin = open(data, 'r')
    s = mat([[0,0,0],[0,0,0],[0,0,0]])
    ptr = 1
    while 1:
        ptr = ptr + 1
        line = fin.readline()
        if not line:
            break
        term = line.split()
        if(term[0] == gender):
            temp1 = mat([[float(term[1])],[float(term[2])],[float(term[3])]]) - m
            temp2 = temp1.T
            s = s + temp1 * temp2
    return s


if __name__ == '__main__':
    m1 = cal_m('data_for_fisher','m')
    print m1
    m2 = cal_m('data_for_fisher','f')
    print m2
    s1 = cal_s('data_for_fisher','m',m1)
    print s1
    s2 = cal_s('data_for_fisher','f',m2)
    print s2
    sw = s1+s2
    w = sw.I * (m1 - m2)
    temp = w[0][0] + w[1][0] + w[2][0]
    answer = w / temp
    threshold = answer.T*(m1+m2)/2
    print answer
    print threshold
    print 'ok!'
