package com.nestlabs.sdk.sample;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class History_data {
    @SerializedName("error_code")
    private Integer error_code;

    @SerializedName("error_msg")
    private String error_msg;

    public History_data(Integer error_code, String error_msg, Data data) {
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @SerializedName("data")
    private Data data;

    public class Data {
        @SerializedName("count")
        private Integer count;

        @SerializedName("time_unit")
        private String time_unit;

        public Data(Integer count, String time_unit, List<Energys> energy) {
            this.count = count;
            this.time_unit = time_unit;
            this.energys = energy;
        }

        public Integer getCount() {

            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getTime_unit() {
            return time_unit;
        }

        public void setTime_unit(String time_unit) {
            this.time_unit = time_unit;
        }

        public List<Energys> getEnergy() {
            return energys;
        }

        public void setEnergy(List<Energys> energy) {
            this.energys = energy;
        }

        @SerializedName("energys")
        private List<Energys> energys;

        public class Energys {
            @SerializedName("date")
            private String date;

            @SerializedName("energy")
            private Double energy;

            public Energys(String date, Double energy) {
                this.date = date;
                this.energy = energy;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public Double getEnergy() {
                return energy;
            }

            public void setEnergy(Double energy) {
                this.energy = energy;
            }
        }
    }

}
