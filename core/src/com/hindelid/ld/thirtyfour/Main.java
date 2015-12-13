package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sun.java2d.opengl.OGLContext;
import sun.reflect.generics.tree.Tree;

public class Main extends ApplicationAdapter {
    private Viewport mViewPort;
    private OrthographicCamera mCamera;
    private ShapeRenderer mShapeRenderer;

    private TreeBranch mRoot;
    public TreeBranch mNextRoot = null;
    private Vector2 mCurrentViewCord;
    private List<Fish> mFishes = new ArrayList<Fish>();
    private List<Octopus> mOctopuses = new ArrayList<Octopus>();
    private HealthMeter mHealthMeter;

    private float mAvgX[] = new float[10];
    private int mAvgXpos = 0;


    private float mSpeed;
    private float mTotalTime = 0f;

    @Override
    public void create() {
        mShapeRenderer = new ShapeRenderer();
        mHealthMeter = new HealthMeter(mShapeRenderer);
        mCamera = new OrthographicCamera();
        mViewPort = new ExtendViewport(Constants.VIEW_SIZE_X, Constants.VIEW_SIZE_Y, mCamera);
        mCurrentViewCord = new Vector2(Constants.VIEW_SIZE_X / 2, Constants.VIEW_SIZE_Y / 2);
        moveAndUpdateCamera();

        resetGame();
    }

    @Override
    public void resize(int aWidth, int aHeight) {
        mViewPort.update(aWidth, aHeight);
        mHealthMeter.resize(aWidth, aHeight);
    }

    private void resetGame() {
        for(int i=0;i<10;i++) {
            mAvgX[i] = 0;
        }
        mRoot = new TreeBranch(
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0),
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0.5f),
                true,
                1);

        mCamera.zoom = 1.0f;
        TreeBranch.sGlobal.setZero();
        mSpeed = Constants.SPEED;
        mHealthMeter.reset();

    }

    @Override
    public void render() {
        long before = TimeUtils.nanoTime();
        tick();
        if (TreeBranch.sGlobal.y < 768f) {
            Gdx.gl.glClearColor(0, 0, TreeBranch.sGlobal.y / 768f, 1);
        } else {
            System.out.println("You beat the game!");
            Gdx.app.exit();
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mShapeRenderer.setProjectionMatrix(mCamera.combined);

        mShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        mShapeRenderer.setColor(Color.BROWN);
        mRoot.render(mShapeRenderer);

        mShapeRenderer.setColor(Color.GREEN);
        mRoot.renderLeefs(mShapeRenderer);

        mShapeRenderer.setColor(Color.WHITE);
        for (Fish f : mFishes) {
            f.render(mShapeRenderer);
        }
        for (Octopus o : mOctopuses) {
            o.render(mShapeRenderer);
        }

        mShapeRenderer.end();

        mHealthMeter.render();

        long after = TimeUtils.nanoTime();


//        System.out.println("y:" + TreeBranch.sGlobal.y + " Time:" + (after - before) / 1000); //TODO remove

        if (TreeBranch.sGlobal.y < 15f) {
            mHealthMeter.renderStartScreen();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void tick() {
        boolean dead = false;
        checkForNewRoot();
        spawnNewStuff();
        moveAndUpdateCamera();
        if (TreeBranch.sNext) {
            TreeBranch.sNext = false;
            mRoot.split();
        }
        Iterator<Fish> fishIter = mFishes.iterator();
        while (fishIter.hasNext()) {
            if (mRoot.checkCollision(fishIter.next().mBoundingBox)) {
                System.out.println("Collision");
                fishIter.remove();
                mHealthMeter.incHP();
                mSpeed *= 1.1f;
            }
        }
        Iterator<Octopus> octoIter = mOctopuses.iterator();
        while (octoIter.hasNext()) {
            if (mRoot.checkCollision(octoIter.next().mBoundingBox)) {
                octoIter.remove();
                System.out.println("Collision");
                if (mHealthMeter.decHP()) {
                    dead = true;
                }
            }
        }

        removeObsticles();
        if (dead) {
            resetGame();
        }
    }

    /**
     * To avoid unnecessary CPU time when change the root.
     */
    private void checkForNewRoot() {
        if (mNextRoot != null && mNextRoot != mRoot) {
            mRoot = mNextRoot;
        }
    }

    private void spawnNewStuff() {
        if (TreeBranch.sGlobal.y < 10f) { //Don't spawn anything in the beginning.
            return;
        }
        float ypos = TreeBranch.sGlobal.y;
        if ((ypos * 150f / 800f + 30f) * Constants.sRandom.nextFloat() < 1f) {
            spawnFishAbove();
        }
        if (Constants.sRandom.nextFloat() * 100f - (50f * ypos / 1024f) < 1f) {
            spawnOctoAbove();
        }
    }

    private void removeObsticles() {
        Iterator<Fish> fishIter = mFishes.iterator();
        while (fishIter.hasNext()) {
            if (fishIter.next().mPos.y < TreeBranch.sGlobal.y - 10f) {
                fishIter.remove();
            }
        }

        Iterator<Octopus> octoIter = mOctopuses.iterator();
        while (octoIter.hasNext()) {
            if (octoIter.next().mPos.y < TreeBranch.sGlobal.y - 10f) {
                octoIter.remove();
            }
        }
    }

    private void spawnFishAbove() {
        boolean collided = false;
        Fish fish = new Fish(Constants.sRandom.nextFloat() * 10f - 5f + TreeBranch.sGlobal.x, TreeBranch.sGlobal.y + 10f);
        for (Octopus o : mOctopuses) {
            if (o.mBoundingBox.overlaps(fish.mBoundingBox)) {
                collided = true;
            }
        }
        for (Fish f : mFishes) {
            if (f.mBoundingBox.overlaps(fish.mBoundingBox)) {
                collided = true;
            }
        }
        if (!collided) {
            mFishes.add(fish);
        }
    }

    private void spawnOctoAbove() {
        boolean collided = false;
        Octopus octopus = new Octopus(Constants.sRandom.nextFloat() * 20f - 10f + TreeBranch.sGlobal.x, TreeBranch.sGlobal.y + 15f);
        for (Octopus o : mOctopuses) {
            if (o.mBoundingBox.overlaps(octopus.mBoundingBox)) {
                collided = true;
            }
        }
        for (Fish f : mFishes) {
            if (f.mBoundingBox.overlaps(octopus.mBoundingBox)) {
                collided = true;
            }
        }
        if (!collided) {
            mOctopuses.add(octopus);
        }
    }

    private void moveAndUpdateCamera() {
        TreeBranch.sGlobal.y += mSpeed;
        if (TreeBranch.sGlobal.y > 10f && mCamera.zoom < 3f) {
            mCamera.zoom += 0.01f;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            spawnFishAbove();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            resetGame();
        }
        mCurrentViewCord.set(getAvgOfLast10X(),TreeBranch.sGlobal.y);
        mCamera.position.set(mCurrentViewCord, 0);
        mCamera.update();
    }

    private float getAvgOfLast10X() {
        mAvgX[mAvgXpos++] = TreeBranch.sGlobal.x;
        mAvgXpos %= 10;
        float avg = 0;
        for (int i=0; i< 10; i++) {
            avg += mAvgX[i];
        }
        avg /= 10f;
        return avg;
    }
}
