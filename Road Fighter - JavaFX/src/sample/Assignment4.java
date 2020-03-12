package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Assignment4 extends Application {

    static double velocity = 0;
    static final int HEIGHT = 512;
    static final int WIDTH = 512;
    static final int CAR_HEIGHT = 90;
    static final int CAR_WIDTH = 50;
    static int numOfPassedCar = 0;
    static int level = 1;
    static int score = 0;
    static final double acceleration = 0.05;

    boolean isEnter;

    static Text scoreText = new Text();
    static Text levelText = new Text();

    ArrayList<String> input = new ArrayList<String>();
    ArrayList<Rectangle> rivalCars = new ArrayList<>();
    ArrayList<Circle> leftTrees = new ArrayList<>();
    ArrayList<Circle> rightTrees = new ArrayList<>();
    ArrayList<Rectangle> lines = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Rectangle car = new Rectangle(270, 400, CAR_WIDTH, CAR_HEIGHT);
        Rectangle grassLeft = new Rectangle(0, 0, 100, HEIGHT);
        Rectangle grassRight = new Rectangle(412, 0, 100, HEIGHT);
        Rectangle road = new Rectangle(100, 0, 312, 512);

        car.setFill(Color.RED);
        grassLeft.setFill(Color.rgb(1, 200, 1));
        grassRight.setFill(Color.rgb(1, 200, 1));
        road.setFill(Color.GRAY);

        scoreText.setX(10);
        scoreText.setY(25);
        levelText.setX(10);
        levelText.setY(40);
        scoreText.setFill(Color.WHITE);
        levelText.setFill(Color.WHITE);
        scoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        levelText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

        primaryStage.setTitle("HUBBM-Racer");
        primaryStage.setScene(scene);

        drawInitial(root, rivalCars, car, lines, leftTrees, rightTrees, grassLeft, grassRight, road);

        AnimationTimer gameTimer = new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        String code = event.getCode().toString();

                        if(!input.contains(code))
                            input.add(code);
                    }
                });

                scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        String code = event.getCode().toString();
                        input.remove(code);
                    }
                });

                if(isEnter)
                {
                    rivalCars.removeAll(rivalCars);
                    velocity = 0;
                    numOfPassedCar = 0;
                    level = 1;
                    score = 0;
                    drawInitial(root, rivalCars, car, lines, leftTrees, rightTrees, grassLeft, grassRight, road);
                    isEnter = false;
                }

                if(lines.get(lines.size()-1).getY() > 30)
                {
                    lines.add(addLine());
                    root.getChildren().add(1, lines.get(lines.size()-1));
                }
                if(rivalCars.get(rivalCars.size()-1).getY() > 150)
                {
                    rivalCars.add(addCar());
                    root.getChildren().add(rivalCars.get(rivalCars.size()-1));
                }
                if(leftTrees.get(leftTrees.size()-1).getCenterY() > 150)
                {
                    if(Math.random() > 0.7)
                    {
                        leftTrees.add(addTree("left", 0));
                        root.getChildren().add((leftTrees.get(leftTrees.size()-1)));
                    }
                }
                if(rightTrees.get(rightTrees.size()-1).getCenterY() > 150)
                {
                    if(Math.random() > 0.7)
                    {
                        rightTrees.add(addTree("right", 0));
                        root.getChildren().add((rightTrees.get(rightTrees.size()-1)));
                    }
                }

                root.getChildren().remove(scoreText);
                root.getChildren().remove(levelText);

                if(!collisionCheck(rivalCars, car))
                {
                    if(input.contains("LEFT"))
                    {
                        if(car.getX()-5 < 100)    car.setX(100);
                        else    car.setX(car.getX()-5);
                    }
                    if(input.contains(("RIGHT")))
                    {
                        if(car.getX()+5 > 412-CAR_WIDTH)  car.setX(412-CAR_WIDTH);
                        else    car.setX(car.getX()+5);
                    }
                    if(input.contains("UP"))
                    {
                        if(level < velocity)    velocity = level+1;
                        velocity += acceleration;
                    }
                    else
                    {
                        if(velocity <= 0)   velocity = 0.0;
                        else    velocity -= acceleration;
                    }
                }

                moveObjects(rivalCars, lines, leftTrees, rightTrees, car, velocity);

                popPassedObjects(rivalCars, lines, root);

                levelScoreHandling();

                if (collisionCheck(rivalCars, car))
                {
                    stop();
                    Text over = new Text();
                    over.setY(100);
                    over.setX(170);
                    over.setTextAlignment(TextAlignment.CENTER);
                    over.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
                    over.setFill(Color.DARKRED);
                    over.setStrokeWidth(3);
                    over.setStroke(Color.BLACK);
                    over.setText("GAME OVER!");

                    Text lastScore = new Text();
                    lastScore.setY(140);
                    lastScore.setX(160);
                    lastScore.setTextAlignment(TextAlignment.CENTER);
                    lastScore.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
                    lastScore.setFill(Color.DARKRED);
                    lastScore.setStrokeWidth(3);
                    lastScore.setStroke(Color.BLACK);
                    lastScore.setText("Your Score: " + score);

                    Text prompt = new Text();
                    prompt.setY(180);
                    prompt.setX(90);
                    prompt.setTextAlignment(TextAlignment.CENTER);
                    prompt.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));
                    prompt.setFill(Color.DARKRED);
                    prompt.setStrokeWidth(3);
                    prompt.setStroke(Color.BLACK);
                    prompt.setText("Press ENTER to restart!");

                    root.getChildren().addAll(over, lastScore, prompt);

                    restart(scene);
                    start();
                    velocity = 0;
                }
                root.getChildren().add(scoreText);
                root.getChildren().add(levelText);
                scoreText.setText("Score: " + score);
                levelText.setText("Level: " + level);
            }
        };
        gameTimer.start();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void restart(Scene scene){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER){
                    isEnter = true;
                }
            }
        });
    }

    private static void levelScoreHandling()
    {
        if(numOfPassedCar == level*5)
            level++;
    }

    private static Rectangle addCar()
    {
        int x = 104 + (int)(Math.random() * 254);
        Rectangle rival = new Rectangle(x, -100, CAR_WIDTH, CAR_HEIGHT);
        rival.setFill(Color.YELLOW);
        return rival;
    }

    private static Rectangle addCar(int yPos)
    {
        int x = 104 + (int)(Math.random() * 254);
        Rectangle rival = new Rectangle(x, yPos, CAR_WIDTH, CAR_HEIGHT);
        rival.setFill(Color.YELLOW);
        return rival;
    }

    private static Rectangle addLine()
    {
        Rectangle line = new Rectangle(256, 0, 5, 20);
        line.setFill(Color.BLACK);
        return line;
    }

    private static Rectangle addLine(int yPos)
    {
        Rectangle line = new Rectangle(256, yPos, 5, 20);
        line.setFill(Color.BLACK);
        return line;
    }

    private static Circle addTree(String sideOfGrass, int yPos)
    {
        sideOfGrass = sideOfGrass.toLowerCase();
        Circle tree = new Circle();
        if(sideOfGrass.equals("left"))
        {
            int x = 20 + (int) (Math.random() * 60);
            tree = new Circle(x, yPos, 20);
            tree.setFill(Color.GREEN);
        }
        if(sideOfGrass.equals("right"))
        {
            int x = 432 + (int) (Math.random() * 60);
            tree = new Circle(x, yPos, 20);
            tree.setFill(Color.GREEN);
        }
        return tree;
    }

    private static void moveObjects(ArrayList<Rectangle> rivalCars, ArrayList<Rectangle> lines, ArrayList<Circle> leftTrees, ArrayList<Circle> rightTrees, Rectangle userCar, double velocity)
    {
        for(Rectangle car : rivalCars)
        {
            car.setY(car.getY() + velocity);
            if(car.getY() >= userCar.getY())
            {
                if(car.getFill() == Color.YELLOW)
                {
                    numOfPassedCar++;
                    score += level;
                }
                car.setFill(Color.GREEN);
            }
        }

        for(Rectangle line : lines)
        {
            line.setY(line.getY() + velocity);
        }

        for(Circle tree : leftTrees)
            tree.setCenterY(tree.getCenterY() + velocity);

        for(Circle tree : rightTrees)
            tree.setCenterY(tree.getCenterY() + velocity);
    }

    private static void popPassedObjects(ArrayList<Rectangle> rivalCars, ArrayList<Rectangle> lines, Group root)
    {
        for(int i = 0; i < rivalCars.size(); i++)
            if (rivalCars.get(i).getY() > HEIGHT)
            {
                root.getChildren().remove(rivalCars.get(i));
                rivalCars.remove(i);
            }

        for(int i = 0; i < lines.size(); i++)
            if(lines.get(i).getY() > HEIGHT)
            {
                root.getChildren().remove(lines.get(i));
                lines.remove(i);
            }
    }

    private static boolean collisionCheck(ArrayList<Rectangle> rivalCars, Rectangle userCar)
    {
        for(int i = 0; i < rivalCars.size(); i++)
        {
            Rectangle car = rivalCars.get(i);

            if(((car.getY() + CAR_HEIGHT) > 400) && (car.getY() < (userCar.getY() + CAR_HEIGHT)))
                if((car.getX() < (userCar.getX() + CAR_WIDTH)) && ((car.getX() + CAR_WIDTH) > userCar.getX()))
                {
                    car.setFill(Color.BLACK);
                    userCar.setFill(Color.BLACK);
                    return true;
                }
        }
        return false;
    }

    private static void drawInitial(Group root, ArrayList<Rectangle> rivalCars, Rectangle userCar,ArrayList<Rectangle> lines, ArrayList<Circle> leftTrees, ArrayList<Circle> rightTrees, Rectangle grassLeft, Rectangle grassRight, Rectangle road)
    {
        // clean the scene for reinitialasing
        root.getChildren().clear();
        root.getChildren().add(0, road);
        userCar.setFill(Color.RED);

        for(int i = 0; i < 512; i+=30)
        {
            Rectangle line = addLine(i);
            lines.add(line);
            root.getChildren().add(line);
        }

        root.getChildren().addAll(userCar, grassLeft, grassRight);

        for(int i = (400 - CAR_HEIGHT - 120); i >=0; i-= (CAR_HEIGHT + 120))
        {
            Rectangle car = addCar(i);
            rivalCars.add(car);
            root.getChildren().add(car);
        }

        for(int i = 50 + ((int)(Math.random() * 150)); i < HEIGHT; i += (90 + (int)(Math.random()*150)))
        {
            Circle tree = addTree("Left", i);
            leftTrees.add(tree);
            root.getChildren().add(tree);
        }

        for(int i = 50 + ((int)(Math.random() * 150)); i < HEIGHT; i += (90 + (int)(Math.random()*150)))
        {
            Circle tree = addTree("Right", i);
            rightTrees.add(tree);
            root.getChildren().add(tree);
        }
    }
}
