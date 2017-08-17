package com.infinity.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class TestGame extends ApplicationAdapter {
	SpriteBatch batch;
	SpriteBatch bat;
	Texture img;
	Texture[] chidiya;
	Texture abtube;
	Texture bltube;
	Circle bCircle;
	Rectangle[] top;
	Rectangle[] bottom;
	ShapeRenderer shapeRenderer;
	Texture over;
	int fS=0;
	Texture resS;

	float yPos;
	float speed=0;
	float grav= (float) 1.54;
	int state=0;
	float gap=500;
	Preferences hScore;
	int hscr;

	BitmapFont hFont;
	float maxY;
	Random r;
	float tubeX=4;
	int ntubes = 8;
	float dist;
	float[] ran=new float[ntubes];
	float[] twidth=new float[ntubes];
	int scr;
	int scrT=0;
	BitmapFont font;
	BitmapFont reset;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		bat = new SpriteBatch();
		img= new Texture("bg.png");
		chidiya= new Texture[2];
		chidiya[0]=new Texture("bird.png");
		chidiya[1]=new Texture("bird2.png");
		over=new Texture("over.png");
		resS=new Texture("red.png");
		font = new BitmapFont();
		font.setColor(Color.YELLOW);
		font.getData().scale(15);
		hFont = new BitmapFont();
		hFont.setColor(Color.RED);
		hFont.getData().scale(20);
		reset = new BitmapFont();
		reset.setColor(Color.FIREBRICK);
		reset.getData().scale(7);
		shapeRenderer=new ShapeRenderer();
		bCircle=new Circle();
		top=new Rectangle[ntubes];
		bottom=new Rectangle[ntubes];

		abtube=new Texture("toptube.png");
		bltube=new Texture("bottomtube.png");
		scr=0;

		maxY = Gdx.graphics.getHeight() - gap/2 - 100;
		r = new Random();

		hScore = Gdx.app.getPreferences("High Score");
		hscr=hScore.getInteger("score",0);

		startGame();

		Gdx.app.log("On Create","Working");
	}

	private void startGame() {


		yPos = Gdx.graphics.getHeight()/2-chidiya[0].getHeight()/2;
		dist = Gdx.graphics.getWidth()*4/6;
		for (int i = 0; i < ntubes; i++) {
			ran[i] = (r.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			twidth[i] = Gdx.graphics.getWidth() / 2 - abtube.getWidth() / 2+Gdx.graphics.getWidth() + i * dist;

			top[i]=new Rectangle();
			bottom[i]=new Rectangle();

		}
	}

	@Override
	public void render () {
		hscr=hScore.getInteger("score",0);

		batch.begin();
		batch.draw(img,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);


		if(state == 1){

			if(twidth[scrT] < Gdx.graphics.getWidth()/2){

				scr++;

				if( scrT < ntubes-1){

					scrT++;

			}
			else{

				scrT=0;

			}

		}

			if(Gdx.input.justTouched()) {

				speed = -30;

			}
			for (int i = 0; i < ntubes; i++) {
				if(twidth[i] < - abtube.getWidth()){

					twidth[i] += ntubes*dist ;

					ran[i] = (r.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}
				else {

					twidth[i] -= tubeX;
				}

				batch.draw(abtube,twidth[i], Gdx.graphics.getHeight()/2  + gap/2 + ran[i]);
				batch.draw(bltube,twidth[i], Gdx.graphics.getHeight()/2 -bltube.getHeight() - gap/2 + ran[i]);
				top[i]=new Rectangle(twidth[i],Gdx.graphics.getHeight()/2  + gap/2 + ran[i],abtube.getWidth(),abtube.getHeight());
				bottom[i]=new Rectangle(twidth[i],Gdx.graphics.getHeight()/2 -bltube.getHeight() - gap/2 + ran[i],bltube.getWidth(),bltube.getHeight());
				}


			if(yPos > 0 ) {


				speed += grav;
				yPos -= speed;
			}else{

				state=2;

			}
			batch.draw(chidiya[fS],Gdx.graphics.getWidth()/2 - chidiya[fS].getWidth()/2,yPos);

			font.draw(batch,String.valueOf(scr),150,Gdx.graphics.getHeight()-300);

		}
		else
			if(state == 0 ) {
				font.draw(batch,String.valueOf(scr),150,Gdx.graphics.getHeight()-300);
				batch.draw(chidiya[fS],Gdx.graphics.getWidth()/2 - chidiya[fS].getWidth()/2,yPos);

			if(Gdx.input.justTouched()) {

				state = 1;

			}
		}
			else if(state == 2){

				if(scr > hscr){
					hScore.putInteger("score",scr);
					hScore.flush();
				}

				batch.draw(over,30,Gdx.graphics.getHeight()-400,1000,300);
				batch.draw(chidiya[fS],Gdx.graphics.getWidth()/2 - chidiya[0].getWidth()/2,Gdx.graphics.getHeight());
				reset.draw(batch,"Tap To Play Again",0,Gdx.graphics.getHeight()/2+400,Gdx.graphics.getWidth(),1,false);
				font.draw(batch,String.valueOf(scr),200,Gdx.graphics.getHeight()/2-100);
				hFont.draw(batch,String.valueOf(hscr),600,Gdx.graphics.getHeight()/2-100);
				if(Gdx.input.justTouched()) {

					state = 1;
					startGame();
					scrT=0;
					scr=0;
					speed=0;

				}
			}


		if(fS==0){

			fS=1;

		}else{

			fS=0;

		}





		bCircle.set(Gdx.graphics.getWidth()/2,yPos + chidiya[0].getHeight()/2,chidiya[0].getWidth()/2);
//		shapeRenderer.circle(bCircle.x,bCircle.y,bCircle.radius);
		for (int i = 0; i < ntubes; i++) {

//			shapeRenderer.rect(twidth[i],Gdx.graphics.getHeight()/2  + gap/2 + ran[i],abtube.getWidth(),abtube.getHeight());
//			shapeRenderer.rect(twidth[i],Gdx.graphics.getHeight()/2 -bltube.getHeight() - gap/2 + ran[i],bltube.getWidth(),bltube.getHeight());

			if(Intersector.overlaps(bCircle,top[i])||Intersector.overlaps(bCircle,bottom[i])){

				state=2;

			}
		}

		shapeRenderer.end();
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		bat.dispose();
	}

//	@Override
//	public void pause() {
//		super.pause();
//	}
//
//	@Override
//	public void resume() {
//		super.resume();
//	}

}
