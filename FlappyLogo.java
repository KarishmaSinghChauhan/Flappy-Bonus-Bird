package com.karishma.flappylogo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyLogo extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Circle birdCircle;
	Rectangle tubeRectangle[];
	ShapeRenderer shapeRenderer;

	Texture birds[];
	Texture topTube;
	Texture bottomTube;
	int flappyState = 1;
	int gameState = 0;
	float birdY;
	float gravity = 3;
	float velocity = 0;

	float distanceBtwTubes;
    int numberOfTubes = 4;
    float tubeX[] = new float[numberOfTubes];
	float tubeY[] = new float[numberOfTubes];
	float gap = 400;
	float tubeVelocity = 4;
	Random randomGenerator;

	int score = 0;
	int presentTube = 0;
	BitmapFont font;
	BitmapFont font2;
	int bonus = 0;



	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("flappy_background.jpg");
		birds = new Texture[2];
		birds[0] = new Texture("flappy_5_2.jpg");
		birds[1] = new Texture("flappy_3_2.png");
		birdY = Gdx.graphics.getHeight()/2-birds[flappyState].getHeight()/2;

		topTube = new Texture("flappy_pipedown2.png");
		bottomTube = new Texture("flappy_pipeup2.png");
		distanceBtwTubes = Gdx.graphics.getWidth()*3/4;
		randomGenerator = new Random();

		tubeRectangle = new Rectangle[2*numberOfTubes];

		for(int i = 0; i<= numberOfTubes-1; i++)
		{
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + (i * distanceBtwTubes) + Gdx.graphics.getWidth()/2;
            tubeY[i] = (randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);

            int j = i*2;
            tubeRectangle[j] = new Rectangle();
			tubeRectangle[j+1] = new Rectangle();
        }

		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();

		font = new BitmapFont();
		font2 = new BitmapFont();

	}

	@Override
	public void render () {

        batch.begin();
        batch.draw(background,0 ,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight() );

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);

		if(gameState == 1)
		{
            for(int i = 0; i<= numberOfTubes -1; i++)
            {
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeY[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeY[i]);

				int j = i*2;

				tubeRectangle[j] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeY[i] , topTube.getWidth(), topTube.getHeight());
				tubeRectangle[j+1] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeY[i], bottomTube.getWidth(), bottomTube.getHeight());

                //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeY[i] , topTube.getWidth(), topTube.getHeight());
				//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeY[i], bottomTube.getWidth(), bottomTube.getHeight());

//				shapeRenderer.rect(tubeRectangle[j].x, tubeRectangle[j].y, tubeRectangle[j].width, tubeRectangle[j].height);
//				shapeRenderer.rect(tubeRectangle[j+1].x, tubeRectangle[j+1].y, tubeRectangle[j+1].width, tubeRectangle[j+1].height);

				if(Intersector.overlaps(birdCircle, tubeRectangle[j]) || Intersector.overlaps(birdCircle, tubeRectangle[j+1]) )
				{
					Gdx.app.log("Collision", "Collided!");
				}
               // tubeY[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200); //code is different for it from the video lecture.
                tubeX[i] = tubeX[i] - tubeVelocity; //code is different for it from the video lecture.


				if(tubeX[i] < - bottomTube.getWidth())
                {
                    tubeX[i] = distanceBtwTubes * numberOfTubes;
					tubeY[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				}
				else if(tubeX[presentTube] < Gdx.graphics.getWidth()/2 - bottomTube.getWidth()/2)
				{
					score += 1;
					Gdx.app.log("Score", Integer.toString(score));

					if(presentTube <= numberOfTubes-2)
					{
						presentTube += 1;
					}
					else
					{
						presentTube = 0;
					}
				}

            }

			if(Gdx.input.justTouched())
			{
				velocity = -40;
			}

			if(birdY > 0 ||velocity < 0)
			{
                velocity = velocity + gravity;
                birdY = birdY - velocity;
            }
		}

		else
		{
			if(Gdx.input.justTouched())
			{
				gameState = 1;
			}
		}


		if(flappyState == 1)
		{
			flappyState = 0;
		}
		else
		{
			flappyState = 1;
		}


		batch.draw(birds[flappyState], Gdx.graphics.getWidth()/2-birds[flappyState].getWidth()/2, birdY);
//		batch.draw(birds[0], Gdx.graphics.getWidth()/2-birds[0].getWidth()/2, birdY);


//		batch.draw(birds[0], Gdx.graphics.getWidth()/2, 0);
//		batch.draw(birds[1], 0, Gdx.graphics.getHeight()/2);

		font.setColor(Color.BLACK);
		font.getData().setScale(5);
		font.draw(batch, "Score: "+ Integer.toString(score), 80, 200);
		font.setColor(Color.BLACK);
		font.getData().setScale(5);
		font.draw(batch, "Bonus: "+ Integer.toString(bonus), 80, 120);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flappyState].getHeight()/2, birds[flappyState].getHeight()/2 );

/*
		for(int i = 0; i<= numberOfTubes-1; i++)
		{
			int j = 1*2;

			shapeRenderer.rect(tubeRectangle[j].x, tubeRectangle[j].y, tubeRectangle[j].width, tubeRectangle[j].height);
			shapeRenderer.rect(tubeRectangle[j+1].x, tubeRectangle[j+1].y, tubeRectangle[j+1].width, tubeRectangle[j+1].height);

//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeY[i] , topTube.getWidth(), topTube.getHeight());
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight(), bottomTube.getWidth(), bottomTube.getHeight());
		}
*/

		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		//shapeRenderer.end();



	}
	
//	@Override
//	public void dispose () {
//		batch.dispose();
////		img.dispose();
//	}
}
