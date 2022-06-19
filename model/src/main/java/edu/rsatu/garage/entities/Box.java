package entities;
public class Box {
    public Box(Integer boxNum, float rentPrice) {
        this.boxNum = boxNum;
        this.rentPrice = rentPrice;
    }

    private Integer boxNum;
    private float rentPrice;


    public Integer getBoxNum() {
        return boxNum;
    }

    public float getRentPrice() {
        return rentPrice;
    }

    public void setBoxNum(Integer boxNum) {
        this.boxNum = boxNum;
    }

    public void setRentPrice(float rentPrice) {
        this.rentPrice = rentPrice;
    }


}
