package machine;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
Simulates a machine that demonstrates the central limit theorem 
*/
public class BeanMachine extends Application
{
    private final BeanMaker beanMaker = new BeanMaker();
    private final SecureRandom random = new SecureRandom();
    private final Pane pane = new Pane();
    private final double SCENE_WIDTH = 500;
    private final double SCENE_HEIGHT = 650;
    
    private final int PEG_DIM = 20;
    private final int NUM_ROWS = 9;
    private final ArrayList<Bean> beanArray = new ArrayList<>();
    
    private final Polyline line = new Polyline();
    private final double STROKE_WIDTH = 5;
    private final double NECK_WIDTH = 20;
    private final double LEFT_NECK_X = SCENE_WIDTH / 2 - NECK_WIDTH ;  
    protected static final double TOP_NECK_Y = 20.0;
    protected static final double BOTTOM_NECK_Y = 100.0;
    private final double LEFT_X = 50; 
    private final double HIP_Y = 450.0;
    private final double BOTTOM_Y = SCENE_HEIGHT - 50; 
    private final double TOP_BIN_Y = HIP_Y + 25;   
    private final double RIGHT_X = SCENE_WIDTH - 50;   
    private final double RIGHT_NECK_X = SCENE_WIDTH / 2 + NECK_WIDTH;

    private final int[] numBeansInStack = new int[NUM_ROWS + 1];
    private final int MAX_BEANS_IN_STACK = 50;
    
    private Timeline animMachine;  //processes individual KeyFrame

    @Override
    public void start(Stage mainStage)      
    {
        pane.setPadding(new Insets(0, 0, 10, 0));
        
        /*
        Animates the machine by passing a new KeyFram object every 100 milliseconds
        */
        animMachine = new Timeline(new KeyFrame(Duration.millis(100), e -> beanMaker.drop()));
        animMachine.setCycleCount(Timeline.INDEFINITE); 

        createContainer();
       
        BorderPane root = new BorderPane();
        root.setCenter(pane);
        root.setBottom(createButtonPane());
        
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        mainStage.setScene(scene);
        mainStage.setTitle("Bean Machine");
        mainStage.show();
    }
    
    public static void main(String[] args)
    {
        Application.launch(args);
    }
    
    /*
    Draws container border, pegs, and bins
    */
    private void createContainer()
    {        
        ObservableList<Double> olLine = line.getPoints();
        olLine.add(LEFT_NECK_X);
        olLine.add(TOP_NECK_Y);
        olLine.add(LEFT_NECK_X);
        olLine.add(BOTTOM_NECK_Y);
        olLine.add(LEFT_X);
        olLine.add(HIP_Y);
        olLine.add(LEFT_X);
        olLine.add(BOTTOM_Y);
        olLine.add(RIGHT_X);
        olLine.add(BOTTOM_Y);
        olLine.add(RIGHT_X);
        olLine.add(HIP_Y);
        olLine.add(RIGHT_NECK_X);
        olLine.add(BOTTOM_NECK_Y);
        olLine.add(RIGHT_NECK_X);
        olLine.add(TOP_NECK_Y);
        line.setStrokeWidth(STROKE_WIDTH);
        pane.getChildren().add(line);
        
        addBins();
        addPegs();       
    }
    
    /*
    Adds bins to container
    */
    private void addBins()
    {
        int binWidth = PEG_DIM * 2;

        for (int i = (int)LEFT_X; i < RIGHT_X; i += binWidth)
        {
            Line wall = new Line(i, TOP_BIN_Y, i, BOTTOM_Y);
            wall.setStrokeWidth(STROKE_WIDTH);
            pane.getChildren().add(wall);       
        }        
    }
    
    /*
    Adds pegs to container
    */
    private void addPegs()
    {
        for (int i = 0; i < NUM_ROWS; i++)
        {
            int x = (int)(SCENE_WIDTH / 2 - PEG_DIM / 2) - (i * PEG_DIM);
            for (int j = 0; j < i + 1; j++, x = x + PEG_DIM * 2)
            {
                Rectangle peg = new Rectangle();
                peg.setWidth(PEG_DIM);
                peg.setHeight(PEG_DIM);
                peg.setX(x);
                peg.setY(i * PEG_DIM * 2 + BOTTOM_NECK_Y + PEG_DIM);
                peg.setFill(Color.color(random.nextDouble(), random.nextDouble(),
                        random.nextDouble()));
                pane.getChildren().add(peg);
            }
        }         
    }
    
    private Node createButtonPane()
    {
        Button btnStart = new Button("Start");
        btnStart.setOnAction(e ->
        {
            animMachine.play();
        });
        Button btnStop = new Button("Stop");
        btnStop.setOnAction(e ->
        {
            animMachine.stop();
        });
        Button btnClear = new Button("Clear");
        btnClear.setOnAction(e ->
        {
            animMachine.stop();
            //Clears beans from scene graph
            for (Bean bean: beanArray)
            {
                pane.getChildren().remove(bean);
            }
            beanArray.clear();
        });
                
        HBox hbButtonPane = new HBox(10);
        hbButtonPane.setPadding(new Insets(10, 0, 20, 0));
        hbButtonPane.setAlignment(Pos.CENTER);
        hbButtonPane.getChildren().addAll(btnStart, btnStop, btnClear);
        
        return hbButtonPane;
    }
    
    /*
    Creates bean and provides bean drop logic
    */
    private class BeanMaker
    {
        private final double BEAN_RADIUS = PEG_DIM / 6;
        private final double START_X = (RIGHT_NECK_X - LEFT_NECK_X) / 2 + LEFT_NECK_X;
        private final double START_Y = TOP_NECK_Y;
        private final double SHIFT_Y = 7;
        private final double SHIFT_X = PEG_DIM;
        //Holds reference to each bean and animation associated with it
        private final Map<Bean, Timeline> mapBeanAnim = new HashMap<>();
        
        /*
        Initiliazes a Bean, passes the reference to the move method which is 
        called by the EventHandler at specified points in the animation timeline
        */
        public void drop()
        {
            Bean bean = initBean();
            Timeline animBallDrop = new Timeline(new KeyFrame(Duration.millis(50), e -> move(bean)));
            animBallDrop.setCycleCount(Timeline.INDEFINITE);
            animBallDrop.play();
            mapBeanAnim.put(bean, animBallDrop);
        }

        public void clear()
        {
            mapBeanAnim.clear();
        }
        
        /*
        Logic that moves the bean
        */
        private void move(Bean bean)
        {
            double centerY = bean.getCenterY();
            if (isInPlay(centerY)) //bean in peg area
            {
                double rowYPlane = bean.getYPlane();
                int rowCount = bean.getRowCount();
                boolean isLeft = randomMoveDirection();
                if (isHitPeg(centerY, rowYPlane, rowCount)) //bean in contact with peg
                {
                    shiftX(bean, isLeft);
                }
                shiftY(bean);
            }
            else if (isClearPegs(centerY))//bean through peg area
            {
                //get the number of left moves the bean has made
                int c = bean.getLeftCount();

                if (isAtBottom(centerY, c)) //bean cannot fall more
                {
                    numBeansInStack[c] += 1;//increase the number of beans in its stack
                    //set centerY of bean based on number of beans already in stack
                    bean.setCenterY(BOTTOM_Y - (BEAN_RADIUS + (numBeansInStack[c])));
                    settleBean(bean);
                    stopAnim(bean);
                    if (isStackFilled(numBeansInStack[c]))
                    {
                        animMachine.stop();
                    }                    
                }
                else //bean must fall further before settling
                {                    
                    shiftY(bean);
                }
            }            
        }
        
        /*
        Checks whether the bean is still within the grid of pegs
        */
        private boolean isInPlay(double centerY)
        {
            return centerY < TOP_BIN_Y - BEAN_RADIUS;
        }
        
        /*
        Checks whether the bean edge is in contact with peg
        */
        private boolean isHitPeg(double centerY, double yPlane, int rowCount)
        {
            return centerY + BEAN_RADIUS >= yPlane && rowCount < NUM_ROWS;
        }
        
        /*
        Checks whether bean is through peg area
        */
        private boolean isClearPegs(double centerY)
        {
            return centerY >= TOP_BIN_Y - BEAN_RADIUS;
        }
        
        /*
        Checks whether bean is at lowest point in drop path
        */
        private boolean isAtBottom(double centerY, int stackNum)
        {
            return centerY >= BOTTOM_Y - (BEAN_RADIUS + numBeansInStack[stackNum]);            
        }
        
        //Create bean
        private Bean initBean()
        {
            Bean bean = new Bean();
            bean.setRadius(BEAN_RADIUS);
            bean.setCenterX(START_X);
            bean.setCenterY(START_Y);
            bean.setFill(Color.color(random.nextDouble(), random.nextDouble(), 
                    random.nextDouble()));
            beanArray.add(bean);
            pane.getChildren().add(bean);
            return bean;
        }
        
        //Generates boolean value to represent left or right bean move
        private boolean randomMoveDirection()
        {
            return random.nextBoolean();
        }
        
        //Moves bean along x-axis
        private void shiftX(Bean bean, boolean isLeft)
        {
            double shiftX;
            if (isLeft)
            {
                shiftX = SHIFT_X * -1;
                bean.setLeftCount(bean.getLeftCount() + 1); //increases left move count
            }
            else
            {
                shiftX = SHIFT_X;
            }
            bean.setCenterX(bean.getCenterX() + shiftX);
            bean.setYPlane(bean.getYPlane() + PEG_DIM * 2); //increases rowY plane
            bean.setRowCount(bean.getRowCount() + 1); //increases row count
        }
        
        //Moves bean along y-axis
        private void shiftY(Bean bean)
        {
            bean.setCenterY(bean.getCenterY() + SHIFT_Y);
        }
        
        //Approximates natural settling motion of a bean dropped onto other beans
        private void settleBean(Bean bean)
        {
            if (random.nextDouble() < .5)
            {
                bean.setCenterX(bean.getCenterX() + BEAN_RADIUS/2 + 
                        (random.nextDouble() * BEAN_RADIUS * 4));
            }
            else
            {
                bean.setCenterX(bean.getCenterX() - BEAN_RADIUS/2 - 
                        (random.nextDouble() * BEAN_RADIUS * 4));
            }
        }
        
        //Stops animation of single bean
        private void stopAnim(Bean bean)
        {
            Timeline animation = mapBeanAnim.get(bean);
            animation.stop();            
        }
        
        //If stack is full, game animation concludes
        private boolean isStackFilled(int numBeans)
        {
            return numBeans >= MAX_BEANS_IN_STACK;
        }
    }
}
