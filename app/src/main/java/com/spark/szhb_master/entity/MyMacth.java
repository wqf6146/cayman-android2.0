package com.spark.szhb_master.entity;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class MyMacth {


    /**
     * gcxMatchBalance : 20.0
     * coins : {"GCA":{"id":2,"coin":{"name":"GCA","nameCn":"银河链A","unit":"GCA","status":0,"minTxFee":0.1,"cnyRate":1,"maxTxFee":0.1,"usdRate":0,"enableRpc":1,"sort":1,"canWithdraw":1,"canRecharge":1,"canTransfer":1,"canAutoWithdraw":1,"withdrawThreshold":10000,"minWithdrawAmount":10,"maxWithdrawAmount":10000,"isPlatformCoin":0,"hasLegal":false,"allBalance":null,"coldWalletAddress":"","hotAllBalance":null,"minerFee":0.05,"withdrawScale":4},"inputRatio":0.5,"outputRatio":1,"dailyLimit":3000,"available":1682,"eachLimit":20},"GCC":{"id":1,"coin":{"name":"GalaxyChain","nameCn":"银河链","unit":"GCC","status":0,"minTxFee":0.2,"cnyRate":0,"maxTxFee":0.2,"usdRate":0,"enableRpc":1,"sort":1,"canWithdraw":1,"canRecharge":1,"canTransfer":1,"canAutoWithdraw":1,"withdrawThreshold":1000,"minWithdrawAmount":0.1,"maxWithdrawAmount":1000,"isPlatformCoin":1,"hasLegal":false,"allBalance":null,"coldWalletAddress":"Ggw4chExgZLMe8iCtftu9hXU86cbeedLCU","hotAllBalance":null,"minerFee":0.01,"withdrawScale":4},"inputRatio":1,"outputRatio":1,"dailyLimit":3000,"available":0,"eachLimit":5},"GCB":{"id":3,"coin":{"name":"GCB","nameCn":"银河链B","unit":"GCB","status":0,"minTxFee":0.1,"cnyRate":1,"maxTxFee":0.1,"usdRate":0,"enableRpc":1,"sort":1,"canWithdraw":1,"canRecharge":1,"canTransfer":1,"canAutoWithdraw":1,"withdrawThreshold":10000,"minWithdrawAmount":10,"maxWithdrawAmount":10000,"isPlatformCoin":0,"hasLegal":false,"allBalance":null,"coldWalletAddress":"","hotAllBalance":null,"minerFee":0.05,"withdrawScale":4},"inputRatio":0.5,"outputRatio":1,"dailyLimit":2000,"available":0.2832,"eachLimit":20}}
     */

    private double gcxMatchBalance;
    private CoinsBean coins;

    public double getGcxMatchBalance() {
        return gcxMatchBalance;
    }

    public void setGcxMatchBalance(double gcxMatchBalance) {
        this.gcxMatchBalance = gcxMatchBalance;
    }

    public CoinsBean getCoins() {
        return coins;
    }

    public void setCoins(CoinsBean coins) {
        this.coins = coins;
    }

    public static class CoinsBean {
        /**
         * GCA : {"id":2,"coin":{"name":"GCA","nameCn":"银河链A","unit":"GCA","status":0,"minTxFee":0.1,"cnyRate":1,"maxTxFee":0.1,"usdRate":0,"enableRpc":1,"sort":1,"canWithdraw":1,"canRecharge":1,"canTransfer":1,"canAutoWithdraw":1,"withdrawThreshold":10000,"minWithdrawAmount":10,"maxWithdrawAmount":10000,"isPlatformCoin":0,"hasLegal":false,"allBalance":null,"coldWalletAddress":"","hotAllBalance":null,"minerFee":0.05,"withdrawScale":4},"inputRatio":0.5,"outputRatio":1,"dailyLimit":3000,"available":1682,"eachLimit":20}
         * GCC : {"id":1,"coin":{"name":"GalaxyChain","nameCn":"银河链","unit":"GCC","status":0,"minTxFee":0.2,"cnyRate":0,"maxTxFee":0.2,"usdRate":0,"enableRpc":1,"sort":1,"canWithdraw":1,"canRecharge":1,"canTransfer":1,"canAutoWithdraw":1,"withdrawThreshold":1000,"minWithdrawAmount":0.1,"maxWithdrawAmount":1000,"isPlatformCoin":1,"hasLegal":false,"allBalance":null,"coldWalletAddress":"Ggw4chExgZLMe8iCtftu9hXU86cbeedLCU","hotAllBalance":null,"minerFee":0.01,"withdrawScale":4},"inputRatio":1,"outputRatio":1,"dailyLimit":3000,"available":0,"eachLimit":5}
         * GCB : {"id":3,"coin":{"name":"GCB","nameCn":"银河链B","unit":"GCB","status":0,"minTxFee":0.1,"cnyRate":1,"maxTxFee":0.1,"usdRate":0,"enableRpc":1,"sort":1,"canWithdraw":1,"canRecharge":1,"canTransfer":1,"canAutoWithdraw":1,"withdrawThreshold":10000,"minWithdrawAmount":10,"maxWithdrawAmount":10000,"isPlatformCoin":0,"hasLegal":false,"allBalance":null,"coldWalletAddress":"","hotAllBalance":null,"minerFee":0.05,"withdrawScale":4},"inputRatio":0.5,"outputRatio":1,"dailyLimit":2000,"available":0.2832,"eachLimit":20}
         */

        private GCABean GCA;
        private GCCBean GCC;
        private GCBBean GCB;

        public GCABean getGCA() {
            return GCA;
        }

        public void setGCA(GCABean GCA) {
            this.GCA = GCA;
        }

        public GCCBean getGCC() {
            return GCC;
        }

        public void setGCC(GCCBean GCC) {
            this.GCC = GCC;
        }

        public GCBBean getGCB() {
            return GCB;
        }

        public void setGCB(GCBBean GCB) {
            this.GCB = GCB;
        }

        public static class GCABean {
            /**
             * id : 2
             * coin : {"name":"GCA","nameCn":"银河链A","unit":"GCA","status":0,"minTxFee":0.1,"cnyRate":1,"maxTxFee":0.1,"usdRate":0,"enableRpc":1,"sort":1,"canWithdraw":1,"canRecharge":1,"canTransfer":1,"canAutoWithdraw":1,"withdrawThreshold":10000,"minWithdrawAmount":10,"maxWithdrawAmount":10000,"isPlatformCoin":0,"hasLegal":false,"allBalance":null,"coldWalletAddress":"","hotAllBalance":null,"minerFee":0.05,"withdrawScale":4}
             * inputRatio : 0.5
             * outputRatio : 1
             * dailyLimit : 3000.0
             * available : 1682.0
             * eachLimit : 20.0
             */

            private int id;
            private CoinBean coin;
            private double inputRatio;
            private int outputRatio;
            private double dailyLimit;
            private double available;
            private double eachLimit;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public CoinBean getCoin() {
                return coin;
            }

            public void setCoin(CoinBean coin) {
                this.coin = coin;
            }

            public double getInputRatio() {
                return inputRatio;
            }

            public void setInputRatio(double inputRatio) {
                this.inputRatio = inputRatio;
            }

            public int getOutputRatio() {
                return outputRatio;
            }

            public void setOutputRatio(int outputRatio) {
                this.outputRatio = outputRatio;
            }

            public double getDailyLimit() {
                return dailyLimit;
            }

            public void setDailyLimit(double dailyLimit) {
                this.dailyLimit = dailyLimit;
            }

            public double getAvailable() {
                return available;
            }

            public void setAvailable(double available) {
                this.available = available;
            }

            public double getEachLimit() {
                return eachLimit;
            }

            public void setEachLimit(double eachLimit) {
                this.eachLimit = eachLimit;
            }

            public static class CoinBean {
                /**
                 * name : GCA
                 * nameCn : 银河链A
                 * unit : GCA
                 * status : 0
                 * minTxFee : 0.1
                 * cnyRate : 1.0
                 * maxTxFee : 0.1
                 * usdRate : 0.0
                 * enableRpc : 1
                 * sort : 1
                 * canWithdraw : 1
                 * canRecharge : 1
                 * canTransfer : 1
                 * canAutoWithdraw : 1
                 * withdrawThreshold : 10000.0
                 * minWithdrawAmount : 10.0
                 * maxWithdrawAmount : 10000.0
                 * isPlatformCoin : 0
                 * hasLegal : false
                 * allBalance : null
                 * coldWalletAddress :
                 * hotAllBalance : null
                 * minerFee : 0.05
                 * withdrawScale : 4
                 */

                private String name;
                private String nameCn;
                private String unit;
                private int status;
                private double minTxFee;
                private double cnyRate;
                private double maxTxFee;
                private double usdRate;
                private int enableRpc;
                private int sort;
                private int canWithdraw;
                private int canRecharge;
                private int canTransfer;
                private int canAutoWithdraw;
                private double withdrawThreshold;
                private double minWithdrawAmount;
                private double maxWithdrawAmount;
                private int isPlatformCoin;
                private boolean hasLegal;
                private Object allBalance;
                private String coldWalletAddress;
                private Object hotAllBalance;
                private double minerFee;
                private int withdrawScale;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getNameCn() {
                    return nameCn;
                }

                public void setNameCn(String nameCn) {
                    this.nameCn = nameCn;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public double getMinTxFee() {
                    return minTxFee;
                }

                public void setMinTxFee(double minTxFee) {
                    this.minTxFee = minTxFee;
                }

                public double getCnyRate() {
                    return cnyRate;
                }

                public void setCnyRate(double cnyRate) {
                    this.cnyRate = cnyRate;
                }

                public double getMaxTxFee() {
                    return maxTxFee;
                }

                public void setMaxTxFee(double maxTxFee) {
                    this.maxTxFee = maxTxFee;
                }

                public double getUsdRate() {
                    return usdRate;
                }

                public void setUsdRate(double usdRate) {
                    this.usdRate = usdRate;
                }

                public int getEnableRpc() {
                    return enableRpc;
                }

                public void setEnableRpc(int enableRpc) {
                    this.enableRpc = enableRpc;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public int getCanWithdraw() {
                    return canWithdraw;
                }

                public void setCanWithdraw(int canWithdraw) {
                    this.canWithdraw = canWithdraw;
                }

                public int getCanRecharge() {
                    return canRecharge;
                }

                public void setCanRecharge(int canRecharge) {
                    this.canRecharge = canRecharge;
                }

                public int getCanTransfer() {
                    return canTransfer;
                }

                public void setCanTransfer(int canTransfer) {
                    this.canTransfer = canTransfer;
                }

                public int getCanAutoWithdraw() {
                    return canAutoWithdraw;
                }

                public void setCanAutoWithdraw(int canAutoWithdraw) {
                    this.canAutoWithdraw = canAutoWithdraw;
                }

                public double getWithdrawThreshold() {
                    return withdrawThreshold;
                }

                public void setWithdrawThreshold(double withdrawThreshold) {
                    this.withdrawThreshold = withdrawThreshold;
                }

                public double getMinWithdrawAmount() {
                    return minWithdrawAmount;
                }

                public void setMinWithdrawAmount(double minWithdrawAmount) {
                    this.minWithdrawAmount = minWithdrawAmount;
                }

                public double getMaxWithdrawAmount() {
                    return maxWithdrawAmount;
                }

                public void setMaxWithdrawAmount(double maxWithdrawAmount) {
                    this.maxWithdrawAmount = maxWithdrawAmount;
                }

                public int getIsPlatformCoin() {
                    return isPlatformCoin;
                }

                public void setIsPlatformCoin(int isPlatformCoin) {
                    this.isPlatformCoin = isPlatformCoin;
                }

                public boolean isHasLegal() {
                    return hasLegal;
                }

                public void setHasLegal(boolean hasLegal) {
                    this.hasLegal = hasLegal;
                }

                public Object getAllBalance() {
                    return allBalance;
                }

                public void setAllBalance(Object allBalance) {
                    this.allBalance = allBalance;
                }

                public String getColdWalletAddress() {
                    return coldWalletAddress;
                }

                public void setColdWalletAddress(String coldWalletAddress) {
                    this.coldWalletAddress = coldWalletAddress;
                }

                public Object getHotAllBalance() {
                    return hotAllBalance;
                }

                public void setHotAllBalance(Object hotAllBalance) {
                    this.hotAllBalance = hotAllBalance;
                }

                public double getMinerFee() {
                    return minerFee;
                }

                public void setMinerFee(double minerFee) {
                    this.minerFee = minerFee;
                }

                public int getWithdrawScale() {
                    return withdrawScale;
                }

                public void setWithdrawScale(int withdrawScale) {
                    this.withdrawScale = withdrawScale;
                }
            }
        }

        public static class GCCBean {
            /**
             * id : 1
             * coin : {"name":"GalaxyChain","nameCn":"银河链","unit":"GCC","status":0,"minTxFee":0.2,"cnyRate":0,"maxTxFee":0.2,"usdRate":0,"enableRpc":1,"sort":1,"canWithdraw":1,"canRecharge":1,"canTransfer":1,"canAutoWithdraw":1,"withdrawThreshold":1000,"minWithdrawAmount":0.1,"maxWithdrawAmount":1000,"isPlatformCoin":1,"hasLegal":false,"allBalance":null,"coldWalletAddress":"Ggw4chExgZLMe8iCtftu9hXU86cbeedLCU","hotAllBalance":null,"minerFee":0.01,"withdrawScale":4}
             * inputRatio : 1
             * outputRatio : 1
             * dailyLimit : 3000.0
             * available : 0.0
             * eachLimit : 5.0
             */

            private int id;
            private CoinBeanX coin;
            private int inputRatio;
            private int outputRatio;
            private double dailyLimit;
            private double available;
            private double eachLimit;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public CoinBeanX getCoin() {
                return coin;
            }

            public void setCoin(CoinBeanX coin) {
                this.coin = coin;
            }

            public int getInputRatio() {
                return inputRatio;
            }

            public void setInputRatio(int inputRatio) {
                this.inputRatio = inputRatio;
            }

            public int getOutputRatio() {
                return outputRatio;
            }

            public void setOutputRatio(int outputRatio) {
                this.outputRatio = outputRatio;
            }

            public double getDailyLimit() {
                return dailyLimit;
            }

            public void setDailyLimit(double dailyLimit) {
                this.dailyLimit = dailyLimit;
            }

            public double getAvailable() {
                return available;
            }

            public void setAvailable(double available) {
                this.available = available;
            }

            public double getEachLimit() {
                return eachLimit;
            }

            public void setEachLimit(double eachLimit) {
                this.eachLimit = eachLimit;
            }

            public static class CoinBeanX {
                /**
                 * name : GalaxyChain
                 * nameCn : 银河链
                 * unit : GCC
                 * status : 0
                 * minTxFee : 0.2
                 * cnyRate : 0.0
                 * maxTxFee : 0.2
                 * usdRate : 0.0
                 * enableRpc : 1
                 * sort : 1
                 * canWithdraw : 1
                 * canRecharge : 1
                 * canTransfer : 1
                 * canAutoWithdraw : 1
                 * withdrawThreshold : 1000.0
                 * minWithdrawAmount : 0.1
                 * maxWithdrawAmount : 1000.0
                 * isPlatformCoin : 1
                 * hasLegal : false
                 * allBalance : null
                 * coldWalletAddress : Ggw4chExgZLMe8iCtftu9hXU86cbeedLCU
                 * hotAllBalance : null
                 * minerFee : 0.01
                 * withdrawScale : 4
                 */

                private String name;
                private String nameCn;
                private String unit;
                private int status;
                private double minTxFee;
                private double cnyRate;
                private double maxTxFee;
                private double usdRate;
                private int enableRpc;
                private int sort;
                private int canWithdraw;
                private int canRecharge;
                private int canTransfer;
                private int canAutoWithdraw;
                private double withdrawThreshold;
                private double minWithdrawAmount;
                private double maxWithdrawAmount;
                private int isPlatformCoin;
                private boolean hasLegal;
                private Object allBalance;
                private String coldWalletAddress;
                private Object hotAllBalance;
                private double minerFee;
                private int withdrawScale;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getNameCn() {
                    return nameCn;
                }

                public void setNameCn(String nameCn) {
                    this.nameCn = nameCn;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public double getMinTxFee() {
                    return minTxFee;
                }

                public void setMinTxFee(double minTxFee) {
                    this.minTxFee = minTxFee;
                }

                public double getCnyRate() {
                    return cnyRate;
                }

                public void setCnyRate(double cnyRate) {
                    this.cnyRate = cnyRate;
                }

                public double getMaxTxFee() {
                    return maxTxFee;
                }

                public void setMaxTxFee(double maxTxFee) {
                    this.maxTxFee = maxTxFee;
                }

                public double getUsdRate() {
                    return usdRate;
                }

                public void setUsdRate(double usdRate) {
                    this.usdRate = usdRate;
                }

                public int getEnableRpc() {
                    return enableRpc;
                }

                public void setEnableRpc(int enableRpc) {
                    this.enableRpc = enableRpc;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public int getCanWithdraw() {
                    return canWithdraw;
                }

                public void setCanWithdraw(int canWithdraw) {
                    this.canWithdraw = canWithdraw;
                }

                public int getCanRecharge() {
                    return canRecharge;
                }

                public void setCanRecharge(int canRecharge) {
                    this.canRecharge = canRecharge;
                }

                public int getCanTransfer() {
                    return canTransfer;
                }

                public void setCanTransfer(int canTransfer) {
                    this.canTransfer = canTransfer;
                }

                public int getCanAutoWithdraw() {
                    return canAutoWithdraw;
                }

                public void setCanAutoWithdraw(int canAutoWithdraw) {
                    this.canAutoWithdraw = canAutoWithdraw;
                }

                public double getWithdrawThreshold() {
                    return withdrawThreshold;
                }

                public void setWithdrawThreshold(double withdrawThreshold) {
                    this.withdrawThreshold = withdrawThreshold;
                }

                public double getMinWithdrawAmount() {
                    return minWithdrawAmount;
                }

                public void setMinWithdrawAmount(double minWithdrawAmount) {
                    this.minWithdrawAmount = minWithdrawAmount;
                }

                public double getMaxWithdrawAmount() {
                    return maxWithdrawAmount;
                }

                public void setMaxWithdrawAmount(double maxWithdrawAmount) {
                    this.maxWithdrawAmount = maxWithdrawAmount;
                }

                public int getIsPlatformCoin() {
                    return isPlatformCoin;
                }

                public void setIsPlatformCoin(int isPlatformCoin) {
                    this.isPlatformCoin = isPlatformCoin;
                }

                public boolean isHasLegal() {
                    return hasLegal;
                }

                public void setHasLegal(boolean hasLegal) {
                    this.hasLegal = hasLegal;
                }

                public Object getAllBalance() {
                    return allBalance;
                }

                public void setAllBalance(Object allBalance) {
                    this.allBalance = allBalance;
                }

                public String getColdWalletAddress() {
                    return coldWalletAddress;
                }

                public void setColdWalletAddress(String coldWalletAddress) {
                    this.coldWalletAddress = coldWalletAddress;
                }

                public Object getHotAllBalance() {
                    return hotAllBalance;
                }

                public void setHotAllBalance(Object hotAllBalance) {
                    this.hotAllBalance = hotAllBalance;
                }

                public double getMinerFee() {
                    return minerFee;
                }

                public void setMinerFee(double minerFee) {
                    this.minerFee = minerFee;
                }

                public int getWithdrawScale() {
                    return withdrawScale;
                }

                public void setWithdrawScale(int withdrawScale) {
                    this.withdrawScale = withdrawScale;
                }
            }
        }

        public static class GCBBean {
            /**
             * id : 3
             * coin : {"name":"GCB","nameCn":"银河链B","unit":"GCB","status":0,"minTxFee":0.1,"cnyRate":1,"maxTxFee":0.1,"usdRate":0,"enableRpc":1,"sort":1,"canWithdraw":1,"canRecharge":1,"canTransfer":1,"canAutoWithdraw":1,"withdrawThreshold":10000,"minWithdrawAmount":10,"maxWithdrawAmount":10000,"isPlatformCoin":0,"hasLegal":false,"allBalance":null,"coldWalletAddress":"","hotAllBalance":null,"minerFee":0.05,"withdrawScale":4}
             * inputRatio : 0.5
             * outputRatio : 1
             * dailyLimit : 2000.0
             * available : 0.2832
             * eachLimit : 20.0
             */

            private int id;
            private CoinBeanXX coin;
            private double inputRatio;
            private int outputRatio;
            private double dailyLimit;
            private double available;
            private double eachLimit;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public CoinBeanXX getCoin() {
                return coin;
            }

            public void setCoin(CoinBeanXX coin) {
                this.coin = coin;
            }

            public double getInputRatio() {
                return inputRatio;
            }

            public void setInputRatio(double inputRatio) {
                this.inputRatio = inputRatio;
            }

            public int getOutputRatio() {
                return outputRatio;
            }

            public void setOutputRatio(int outputRatio) {
                this.outputRatio = outputRatio;
            }

            public double getDailyLimit() {
                return dailyLimit;
            }

            public void setDailyLimit(double dailyLimit) {
                this.dailyLimit = dailyLimit;
            }

            public double getAvailable() {
                return available;
            }

            public void setAvailable(double available) {
                this.available = available;
            }

            public double getEachLimit() {
                return eachLimit;
            }

            public void setEachLimit(double eachLimit) {
                this.eachLimit = eachLimit;
            }

            public static class CoinBeanXX {
                /**
                 * name : GCB
                 * nameCn : 银河链B
                 * unit : GCB
                 * status : 0
                 * minTxFee : 0.1
                 * cnyRate : 1.0
                 * maxTxFee : 0.1
                 * usdRate : 0.0
                 * enableRpc : 1
                 * sort : 1
                 * canWithdraw : 1
                 * canRecharge : 1
                 * canTransfer : 1
                 * canAutoWithdraw : 1
                 * withdrawThreshold : 10000.0
                 * minWithdrawAmount : 10.0
                 * maxWithdrawAmount : 10000.0
                 * isPlatformCoin : 0
                 * hasLegal : false
                 * allBalance : null
                 * coldWalletAddress :
                 * hotAllBalance : null
                 * minerFee : 0.05
                 * withdrawScale : 4
                 */

                private String name;
                private String nameCn;
                private String unit;
                private int status;
                private double minTxFee;
                private double cnyRate;
                private double maxTxFee;
                private double usdRate;
                private int enableRpc;
                private int sort;
                private int canWithdraw;
                private int canRecharge;
                private int canTransfer;
                private int canAutoWithdraw;
                private double withdrawThreshold;
                private double minWithdrawAmount;
                private double maxWithdrawAmount;
                private int isPlatformCoin;
                private boolean hasLegal;
                private Object allBalance;
                private String coldWalletAddress;
                private Object hotAllBalance;
                private double minerFee;
                private int withdrawScale;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getNameCn() {
                    return nameCn;
                }

                public void setNameCn(String nameCn) {
                    this.nameCn = nameCn;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public double getMinTxFee() {
                    return minTxFee;
                }

                public void setMinTxFee(double minTxFee) {
                    this.minTxFee = minTxFee;
                }

                public double getCnyRate() {
                    return cnyRate;
                }

                public void setCnyRate(double cnyRate) {
                    this.cnyRate = cnyRate;
                }

                public double getMaxTxFee() {
                    return maxTxFee;
                }

                public void setMaxTxFee(double maxTxFee) {
                    this.maxTxFee = maxTxFee;
                }

                public double getUsdRate() {
                    return usdRate;
                }

                public void setUsdRate(double usdRate) {
                    this.usdRate = usdRate;
                }

                public int getEnableRpc() {
                    return enableRpc;
                }

                public void setEnableRpc(int enableRpc) {
                    this.enableRpc = enableRpc;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public int getCanWithdraw() {
                    return canWithdraw;
                }

                public void setCanWithdraw(int canWithdraw) {
                    this.canWithdraw = canWithdraw;
                }

                public int getCanRecharge() {
                    return canRecharge;
                }

                public void setCanRecharge(int canRecharge) {
                    this.canRecharge = canRecharge;
                }

                public int getCanTransfer() {
                    return canTransfer;
                }

                public void setCanTransfer(int canTransfer) {
                    this.canTransfer = canTransfer;
                }

                public int getCanAutoWithdraw() {
                    return canAutoWithdraw;
                }

                public void setCanAutoWithdraw(int canAutoWithdraw) {
                    this.canAutoWithdraw = canAutoWithdraw;
                }

                public double getWithdrawThreshold() {
                    return withdrawThreshold;
                }

                public void setWithdrawThreshold(double withdrawThreshold) {
                    this.withdrawThreshold = withdrawThreshold;
                }

                public double getMinWithdrawAmount() {
                    return minWithdrawAmount;
                }

                public void setMinWithdrawAmount(double minWithdrawAmount) {
                    this.minWithdrawAmount = minWithdrawAmount;
                }

                public double getMaxWithdrawAmount() {
                    return maxWithdrawAmount;
                }

                public void setMaxWithdrawAmount(double maxWithdrawAmount) {
                    this.maxWithdrawAmount = maxWithdrawAmount;
                }

                public int getIsPlatformCoin() {
                    return isPlatformCoin;
                }

                public void setIsPlatformCoin(int isPlatformCoin) {
                    this.isPlatformCoin = isPlatformCoin;
                }

                public boolean isHasLegal() {
                    return hasLegal;
                }

                public void setHasLegal(boolean hasLegal) {
                    this.hasLegal = hasLegal;
                }

                public Object getAllBalance() {
                    return allBalance;
                }

                public void setAllBalance(Object allBalance) {
                    this.allBalance = allBalance;
                }

                public String getColdWalletAddress() {
                    return coldWalletAddress;
                }

                public void setColdWalletAddress(String coldWalletAddress) {
                    this.coldWalletAddress = coldWalletAddress;
                }

                public Object getHotAllBalance() {
                    return hotAllBalance;
                }

                public void setHotAllBalance(Object hotAllBalance) {
                    this.hotAllBalance = hotAllBalance;
                }

                public double getMinerFee() {
                    return minerFee;
                }

                public void setMinerFee(double minerFee) {
                    this.minerFee = minerFee;
                }

                public int getWithdrawScale() {
                    return withdrawScale;
                }

                public void setWithdrawScale(int withdrawScale) {
                    this.withdrawScale = withdrawScale;
                }
            }
        }
    }
}
