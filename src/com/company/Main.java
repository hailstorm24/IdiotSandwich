package com.company;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.IOException;



public class Main implements Runnable, KeyListener {
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;
    public Graphics2D g;
    public boolean startScreen = false;
    public boolean cookScreen = false;
    public boolean finalScreen = true;

    public String gordonMessage = "";


    ArrayList<String> ingredientsInSandwich = new ArrayList<String>();

    public static void main(String[] args) {
        Main ex = new Main();
        new Thread(ex).start();
        String prompt = "Translate 'Hello, world!' to Spanish.";
        queryGptApi(prompt);
    }

    public Main() {

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

    private void setUpGraphics() {
        frame = new JFrame("Application Template");
        panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.setLayout(null);

        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        canvas.addKeyListener(this);

        frame.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                Component c = (Component)evt.getSource();
                canvas.setBounds(0,0,frame.getWidth(),frame.getHeight());
            }
        });

        g = (Graphics2D) bufferStrategy.getDrawGraphics();

        System.out.println("DONE graphic setup");
    }

    public static void queryGptApi(String prompt) {
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
            return (response.body()["choices"][0]["message"]["content"]);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String gordonMessage(ArrayList ingredients){
        return ("Oh, for heaven's sake! What is this? A sandwich or a salad in disguise? Where's the substance? Where's the flavor? It's like a dull symphony with only two notes! Tomatoes and lettuce do not a sandwich make. And this... points at baguette This baguette is as soft as a pillow! It's meant to have crunch, texture, character! It's like eating air! If you want to make a sandwich, you've got to put some heart into it! Add some protein, some cheese, a bit of sauce perhaps! Give it some depth, some personality! Otherwise, it's just a sad excuse for a meal. Disappointing!\n");
    }


    public void renderStartScreen(){
        g.drawRect(100,100,100,100);
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
}