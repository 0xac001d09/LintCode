package 序列型;

/**
 * 序列+状态型dp
 *
 * 最多只允许完成2笔交易
 *
 * 【分析】
 * 只允许完成两笔交易，其实就是需要记录交易了几次，从最后一步来看，就是直接卖出，需要枚举最后一次卖出是在第几天，但是不知道之前有没有卖卖过
 * 所以需要记录状态，分为五个阶段 第一次买之前、第一次持有股票、第一次卖后空档期、第二次持有股票、第二次卖完后的空档期，画张图就知道了
 * 阶段1、3、5都是未持有股票阶段，阶段2、4是持有股票阶段
 * 对于未持有股票阶段（1、3、5），有两种可能，一种是继续保持未持有的状态，另一种就是从持有股票的阶段过来，卖掉，变为未持有的状态
 * 对于持有股票的阶段（2、4），也有两种可能，一是继续保持持有的状态（获利还是亏都有可能，因此要加上），第二种是从原来未持有的状态过来，变为持有
 *
 * dp[i][j]，代表第i支股票处于j状态获得的最大利润 开n+1 状态+1
 * 对于阶段1、3、5, dp[i][j] = max{dp[i-1][j], dp[i-1][j-1] + prices[i-1] - prices[i-2]}
 * 对于阶段2、4, dp[i][j] = max{dp[i-1][j] + prices[i-1] - prices[i-2], dp[i-1][j-1]}
 */
public class _151_买卖股票的最佳时机III {

    public int maxProfit(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n + 1][5+1];   //这边开了状态+1

        //初始化第0支股票，这里很关键，第一个阶段为0，后面都是负无穷大
        dp[0][1] = 0;
        for (int j = 2; j <= 5; j++) {
            dp[0][j] = Integer.MIN_VALUE;
        }
        //从第一支股票开始
        for (int i = 1; i <= n; i++) {

            //阶段1、3、5,不持有股票
            for (int j = 1; j <= 5; j += 2) {
                //初始化继续保持不持有股票的状态
                dp[i][j] = dp[i - 1][j];
                //对于第一阶段，就肯定是未持有的，，上面那句就是处理第一阶段的，此时要处理3、5阶段,如果要变为持有，那上一个阶段不能为无穷小
                if (i >= 2 && j >= 3 && dp[i - 1][j - 1] != Integer.MIN_VALUE) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - 1] + prices[i - 1] - prices[i - 2]);
                }
            }
            //阶段2、4
            for (int j = 2; j <= 4; j += 2) {
                //初始化从上一个阶段变为持有,而不是继续保持状态..
                dp[i][j] = dp[i - 1][j - 1];
                //如果要继续持有，必须保证上一个阶段不能为Integer.MIN_VALUE
                if (i >= 2 && dp[i - 1][j] != Integer.MIN_VALUE) {
                    dp[i][j] = Math.max(dp[i - 1][j] + prices[i - 1] - prices[i - 2], dp[i - 1][j - 1]);
                }
            }
        }
        return Math.max(dp[n][1], Math.max(dp[n][3], dp[n][5]));
    }
}