package com.company;

import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;




public class Main implements Runnable, KeyListener, MouseListener {
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;
    public Graphics2D g;
    public boolean startScreen = true;
    public boolean cookScreen = false;
    public boolean finalScreen = false;
    public Image StartScreen;
    public Rectangle mouseRec;


    public String gordonMessage = "";


    public ArrayList<String> ingredientsInSandwich = new ArrayList<String>();

    public static void main(String[] args) {
        Main ex = new Main();
        new Thread(ex).start();
    }

    public Main() {
            File imagePath = new File("/Users/hailstorm/IdeaProjects/idiotSandwich/IdiotSandwich/start_screen.png");
        System.out.println("Current working directory: " + new File(".").getAbsolutePath());
        StartScreen = Toolkit.getDefaultToolkit().getImage("start_screen.png");
        try {
                // Adjust the path as per your file location
                if (imagePath.exists()) {
                    StartScreen = ImageIO.read(imagePath);
                } else {
                    System.out.println("File not found at specified path: " + imagePath.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load start screen image.");

            }
        ingredientsInSandwich.add("Tomatoes");
        ingredientsInSandwich.add("Tomatoes");

//        System.out.println(gordonMessage(ingredientsInSandwich));
        setUpGraphics();

    }

    public void run() {
        while (true) {
            moveThings();
            render();
            pause(20);
        }
    }


    public void pause(int time ){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

    public void moveThings()
    {

    }

    private void setUpGraphics() {        this.frame = new JFrame("Application Template");
        this.panel = (JPanel)this.frame.getContentPane();
        this.panel.setPreferredSize(new Dimension(1000, 700));
        this.panel.setLayout((LayoutManager)null);
        this.canvas = new Canvas();
        this.canvas.setBounds(0, 0, 1000, 700);
        this.canvas.setIgnoreRepaint(true);
        this.panel.add(this.canvas);
        this.frame.setDefaultCloseOperation(3);
        this.frame.pack();
        this.frame.setResizable(true);
        this.frame.setVisible(true);
        this.canvas.createBufferStrategy(2);
        this.bufferStrategy = this.canvas.getBufferStrategy();
        this.canvas.requestFocus();
        this.canvas.addKeyListener(this);
        this.canvas.addMouseListener(this);
        this.frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component)evt.getSource();
                Main.this.canvas.setBounds(0, 0, Main.this.frame.getWidth(), Main.this.frame.getHeight());
            }
        });
        this.g = (Graphics2D)this.bufferStrategy.getDrawGraphics();
        this.mouseRec = new Rectangle(-110, 0, 0, 0);
        System.out.println("DONE graphic setup");
    }


    public String queryGptApi(String prompt) {
        // Your API key from OpenAI
        String apiKey = "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";

        // Setup the HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Setup the HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}"))
                .build();

        try {
            // Send the request and receive the response
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            // Navigate through the JSON to extract the desired content
            String content = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            return content;
        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    public String gordonMessage(ArrayList ingredients){
        String prompt = "You are Gordon Ramsay. You are presented with a plate that, in a stack from bottom to top, contains: ";
        for (Object item:ingredients){
            prompt += ((String)item+", ");
        }
        prompt += "and nothing else. Critique the sandwich.";
        System.out.println(prompt);
        return (queryGptApi(prompt));
    }

    public void renderStartScreen(){
        g.drawImage(StartScreen, 0,0,1000, 700,null);
    }

    public void renderCookScreen() {
        Graphics g = bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());

        int boxWidth = (frame.getWidth() - 510) / 3;
        int boxHeight = frame.getHeight() / 5;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 3; col++) {
                int x = 510 + col * boxWidth;
                int y = row * boxHeight;
                g.drawRect(x, y, boxWidth, boxHeight);
            }
        }

        g.drawLine(508, 0, 508, frame.getHeight());

    }

    public void renderJudgeScreen() {
        g.drawRect(100,100,100,100);}

    private void render() {
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());

        if(startScreen){
            g.drawImage(StartScreen, 0,0,1000, 700,null);
            renderStartScreen();
        }
        if(cookScreen){
           renderCookScreen();
        }
        if(finalScreen){
            renderJudgeScreen();
        }

        g.dispose();

        bufferStrategy.show();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(startScreen) {
            System.out.println("oooo");
            int xhold = e.getX();
            int yhold = e.getY();
            System.out.println(xhold);
            this.mouseRec = new Rectangle(xhold, yhold, 1, 1);
            this.startScreen = false;
            this.cookScreen = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}