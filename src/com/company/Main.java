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
import java.util.HashMap;
import java.util.Map;


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

    public Image baguette;
    public Image cheese;
    public Image ham;
    public Image honey_mustard;
    public Image lettuce;
    public Image mayo;
    public Image pancake;
    public Image salami;
    public Image tomato;
    public Image turkey;
    public Image waffle;
    public Image white_bread;
    public Image wheat_bread;
    public Image onion;
    public Image pickles;

    public Image yelp;

    Map<String, Image> dictionary = new HashMap<>();



    public Rectangle mouseRec;


    public String gordonMessage = "";


    public ArrayList<String> ingredientsInSandwich = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        Main ex = new Main();
        new Thread(ex).start();
    }

    public Main() throws IOException {
        defineImages();
        setUpGraphics();

    }

    public void defineImages() throws IOException {
        StartScreen = ImageIO.read(new File(new File(".").getAbsolutePath() + "/start_screen.png"));
        CookScreen = ImageIO.read(new File(new File(".").getAbsolutePath() +"/kitchen-21.png"));
        yelp = ImageIO.read(new File(new File(".").getAbsolutePath() +"/yelp_review.png"));

        baguette = ImageIO.read(new File(new File(".").getAbsolutePath() + "/baguette.png"));
        cheese = ImageIO.read(new File(new File(".").getAbsolutePath() + "/cheese.png"));
        ham = ImageIO.read(new File(new File(".").getAbsolutePath() + "/ham.png"));
        honey_mustard = ImageIO.read(new File(new File(".").getAbsolutePath() + "/honey_mustard.png"));
        lettuce = ImageIO.read(new File(new File(".").getAbsolutePath() + "/lettuce.png"));
        mayo = ImageIO.read(new File(new File(".").getAbsolutePath() + "/mayo.png"));
        pancake = ImageIO.read(new File(new File(".").getAbsolutePath() + "/pancake.png"));
        salami = ImageIO.read(new File(new File(".").getAbsolutePath() + "/salami.png"));
        tomato = ImageIO.read(new File(new File(".").getAbsolutePath() + "/tomato.png"));
        turkey = ImageIO.read(new File(new File(".").getAbsolutePath() + "/turkey.png"));
        waffle = ImageIO.read(new File(new File(".").getAbsolutePath() + "/waffle.png"));
        white_bread = ImageIO.read(new File(new File(".").getAbsolutePath() + "/white_bread.png"));
        wheat_bread = ImageIO.read(new File(new File(".").getAbsolutePath() + "/whole_wheat_bread.png"));
        onion = ImageIO.read(new File(new File(".").getAbsolutePath() + "/onion-22.png"));
        pickles = ImageIO.read(new File(new File(".").getAbsolutePath() + "/pickles.png"));

        dictionary.put("pickle", pickles);
        dictionary.put("cheese", cheese);
        dictionary.put("lettuce", lettuce);
        dictionary.put("tomato", tomato);
        dictionary.put("onion", onion);
        dictionary.put("ham", ham);
        dictionary.put("turkey", turkey);
        dictionary.put("salami", salami);
        dictionary.put("honey mustard", honey_mustard);
        dictionary.put("mayo", mayo);
        dictionary.put("pancake bread", pancake);
        dictionary.put("grainy bread", wheat_bread);
        dictionary.put("wonder bread", white_bread);
        dictionary.put("bubble bread", baguette);
        dictionary.put("waffle bread", waffle);
    }

    public void run() {
        while (true) {
            moveThings();
            render();
            pause(5);
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

    public void drawWrappedText(Graphics g, String text, int x, int y, int width) {
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();

        int curX = x;
        int curY = y + lineHeight;

        String[] words = text.split(" ");
        for (String word : words) {
            // Check if adding the next word exceeds the width
            int wordWidth = fm.stringWidth(word + " ");
            if (curX + wordWidth >= x + width) {
                // Move to the next line if the word exceeds the current line width
                curX = x;
                curY += lineHeight;
            }
            g2d.drawString(word, curX, curY);
            curX += wordWidth;
        }
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
        prompt += "and nothing else. Critique the sandwich. Be as creative as possible, and make sure to call the chef an 'idiot sandwich'";
        System.out.println(prompt);
        return (queryGptApi(prompt));
    }

    public void renderStartScreen(){
        g.drawImage(StartScreen, 0,0,1000, 700,null);
    }

    public void renderCookScreen() {
        g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
        g.drawImage(CookScreen, 0,0,1000, 700,null);
        int ypos = 300;
        ArrayList tempIngredients = new ArrayList<String>(ingredientsInSandwich);

        for (Object item : tempIngredients) {
            g.drawImage(dictionary.get((String)item), 0, ypos, 750, 525, null);
            ypos -= 50;
        }
    }


    public void renderJudgeScreen() {
        g.drawImage(yelp, 0,0,1000, 700,null);
        drawWrappedText(g, gordonMessage, 300, 230, 450);
    }

    private void render() {
        Graphics2D g = (Graphics2D)this.bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, 1000, 700);
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

    @Override
    public void mouseReleased(MouseEvent e) {
        if (startScreen) {
            int xhold = e.getX();
            int yhold = e.getY();
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
            if (x < 140 && x > 30 && y < 680 && y > 610) {
                cookScreen = false;
                finalScreen = true;
                gordonMessage = gordonMessage(ingredientsInSandwich);
            }
            if (x < 125 && x > 25 && y < 150 && y > 25 && ingredientsInSandwich.size()>0) {
                ingredientsInSandwich.remove(ingredientsInSandwich.size() - 1);
            }

        }
        if (finalScreen) {
            int x = e.getX();
            int y = e.getY();
            System.out.println(x+" "+y);
            if(x < 200 && x > 30 && y < 630 && y > 580){
                startScreen = true;
                finalScreen = false;
                gordonMessage = "";
                ingredientsInSandwich = new ArrayList<>();
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}