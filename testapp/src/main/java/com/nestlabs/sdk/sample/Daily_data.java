package com.nestlabs.sdk.sample;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Daily_data {
    @SerializedName("error_code")
    private Integer error_code;

    @SerializedName("error_msg")
    private String error_msg;

    @SerializedName("data")
    private String data;

    public Daily_data(Integer error_code, String error_msg, String data) {
        this.error_code = error_code;
        this.error_msg = error_msg;
        this.data = data;
    }

    public Integer getError_code() {
        return error_code;
    }

    public void setError_code(Integer error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("count")
        private Integer count;

        public Data(Integer count, List<Powers> powers) {
            this.count = count;
            this.powers = powers;
        }

        public Integer getCount() {

            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Powers> getPowers() {
            return powers;
        }

        public void setPowers(List<Powers> powers) {
            this.powers = powers;
        }

        @SerializedName("powers")
        private List<Powers> powers;

        public class Powers {
            @SerializedName("time")
            private String time;

            public Powers(String time, Double power) {
                this.time = time;
                this.power = power;
            }

            public String getTime() {

                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public Double getPower() {
                return power;
            }

            public void setPower(Double power) {
                this.power = power;
            }

            @SerializedName("power")
            private Double power;
        }
    }
}
