import math
import random

class CostOfProductionMaths:

    @staticmethod
    def totalFactorProductivity(totalRevenue, totalCost):
        return (math.log1p(totalRevenue) / math.log1p(totalCost))

    @staticmethod
    def cobbDouglasProductionFunction(totalRevenue, totalCost, K, L):
        # Random α and β
        alpha = random.uniform(0.3, 0.4)
        beta  = 1 - alpha

        tfp = CostOfProductionMaths.totalFactorProductivity(totalRevenue, totalCost)
        K = (K + 1) ** alpha * math.log1p(K)
        L = (L + 1) ** beta  * math.sqrt(L)

        # Total output
        return tfp * K * L