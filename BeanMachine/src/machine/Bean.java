package machine;


import javafx.scene.shape.Circle;

/*
Represents a bean in the machine
*/
public class Bean extends Circle
{
    private double yPlane = BeanMachine.BOTTOM_NECK_Y + BeanMachine.TOP_NECK_Y;
    private int rowCount = 0;
    private int rightCount = 0;
    private int leftCount = 0;
    private int counter = 0;

    public double getYPlane() {
        return yPlane;
    }

    public void setYPlane(double yPlane) {
        this.yPlane = yPlane;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }

    public int getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    
}
