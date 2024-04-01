package com.company;

//import org.json.JSONException;
//import org.json.JSONObject;

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
    public Image CookScreen;

    public Rectangle mouseRec;


    public String gordonMessage = "";


    public ArrayList<String> ingredientsInSandwich = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        Main ex = new Main();
        new Thread(ex).start();
    }

    public Main() throws IOException {
        StartScreen = ImageIO.read(new File(new File(".").getAbsolutePath() + "/start_screen.png"));
        CookScreen = ImageIO.read(new File(new File(".").getAbsolutePath() + "/kitchen-21.png"));
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


    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

    public void moveThings() {

    }

    private void setUpGraphics() {
        this.frame = new JFrame("Application Template");
        this.panel = (JPanel) this.frame.getContentPane();
        this.panel.setPreferredSize(new Dimension(1000, 700));
        this.panel.setLayout((LayoutManager) null);
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
                Component c = (Component) evt.getSource();
                Main.this.canvas.setBounds(0, 0, Main.this.frame.getWidth(), Main.this.frame.getHeight());
            }
        });
        this.g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
        this.mouseRec = new Rectangle(-110, 0, 0, 0);
        System.out.println("DONE graphic setup");
    }


//    public String queryGptApi(String prompt) {
//        // Your API key from OpenAI
//        String apiKey = "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";
//
//        // Setup the HttpClient
//        HttpClient client = HttpClient.newHttpClient();
//
//        // Setup the HttpRequest
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
//                .header("Content-Type", "application/json")
//                .header("Authorization", "Bearer " + apiKey)
//                .POST(HttpRequest.BodyPublishers.ofString("{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}"))
//                .build();
//
//        try {
//            // Send the request and receive the response
//            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
//            JSONObject jsonResponse = new JSONObject(response.body());
//            // Navigate through the JSON to extract the desired content
//            String content = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
//            return content;
//        } catch (IOException | InterruptedException | JSONException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }


    public String gordonMessage(ArrayList ingredients) {
        String prompt = "You are Gordon Ramsay. You are presented with a plate that, in a stack from bottom to top, contains: ";
        for (Object item : ingredients) {
            prompt += ((String) item + ", ");
        }
        prompt += "and nothing else. Critique the sandwich.";
        System.out.println(prompt);
        return ("hi");
//        return (queryGptApi(prompt));
    }

    public void renderStartScreen() {
        g.drawImage(StartScreen, 0, 0, 1000, 700, null);
    }

    public void renderCookScreen() {
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
        g.drawImage(CookScreen, 0, 0, 1000, 700, null);

//        int boxWidth = (frame.getWidth() - 510) / 3;
//        int boxHeight = frame.getHeight() / 5;
//
//        for (int row = 0; row < 5; row++) {
//            for (int col = 0; col < 3; col++) {
//                int x = 510 + col * boxWidth;
//                int y = row * boxHeight;
//                g.drawRect(x, y, boxWidth, boxHeight);
//            }
//        }
//
//        g.drawLine(508, 0, 508, frame.getHeight());

    }

    public void renderJudgeScreen() {
        g.drawRect(100, 100, 100, 100);
    }

    private void render() {
        Graphics2D g = (Graphics2D) this.bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, 1000, 700);
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());

        if (startScreen) {
            renderStartScreen();
        }
        if (cookScreen) {
//            g.drawImage(CookScreen, 0,0,1000, 700,null);
            renderCookScreen();
        }
        if (finalScreen) {
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
        if (startScreen) {
            System.out.println("oooo");
            int xhold = e.getX();
            int yhold = e.getY();
            System.out.println(xhold);
            this.mouseRec = new Rectangle(xhold, yhold, 1, 1);
            this.startScreen = false;
            this.cookScreen = true;
        }
        if (cookScreen) {
            int x = e.getX();
            int y = e.getY();
            if (x < 1000 && x > 880 && y < 140 && y > 0) {
                ingredientsInSandwich.add("wonder bread");
                System.out.println("wonder bread");
            }
            if (x < 1000 && x > 880 && y < 280 && y > 140) {
                ingredientsInSandwich.add("bubble bread");
                System.out.println("bubble bread");
            }
            if (x < 1000 && x > 880 && y < 420 && y > 280) {
                ingredientsInSandwich.add("grainy bread");
                System.out.println("grainy bread");
            }
            if (x < 1000 && x > 880 && y < 560 && y > 420) {
                ingredientsInSandwich.add("waffle bread");
                System.out.println("waffle bread");
            }
            if (x < 1000 && x > 880 && y < 700 && y > 560) {
                ingredientsInSandwich.add("pancake bread");
                System.out.println("pancake bread");
            }
            if (x < 880 && x > 760 && y < 140 && y > 0) {
                ingredientsInSandwich.add("honey mustard");
                System.out.println("honey mustard");
            }
            if (x < 880 && x > 760 && y < 280 && y > 140) {
                ingredientsInSandwich.add("mayo");
                System.out.println("mayo");
            }
                if (x < 880 && x > 760 && y < 420 && y > 280) {
                    ingredientsInSandwich.add("salami");
                    System.out.println("salami");
                }
                if (x < 880 && x > 760 && y < 560 && y > 420) {
                    ingredientsInSandwich.add("turkey");
                    System.out.println("turkey");
                }
                if (x < 880 && x > 760 && y < 700 && y > 560) {
                    ingredientsInSandwich.add("ham");
                    System.out.println("ham");
                }
            if (x < 760 && x > 640 && y < 140 && y > 0) {
                ingredientsInSandwich.add("onion");
                System.out.println("onion");
            }
            if (x < 760 && x > 640 && y < 280 && y > 140) {
                ingredientsInSandwich.add("tomato");
                System.out.println("tomato");
            }
            if (x < 760 && x > 640 && y < 420 && y > 280) {
                ingredientsInSandwich.add("cheese");
                System.out.println("cheese");
            }
            if (x < 760 && x > 640 && y < 560 && y > 420) {
                ingredientsInSandwich.add("pickle");
                System.out.println("pickle");
            }
            if (x < 760 && x > 640 && y < 700 && y > 560) {
                ingredientsInSandwich.add("lettuce");
                System.out.println("lettuce");
            }
            }
        }

        @Override
        public void mousePressed (MouseEvent e){

        }

        @Override
        public void mouseReleased (MouseEvent e){

        }

        @Override
        public void mouseEntered (MouseEvent e){

        }

        @Override
        public void mouseExited (MouseEvent e){

        }


}