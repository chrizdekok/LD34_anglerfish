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

    private float mSpeed;
    private float mTotalTime = 0f;

    @Override
    public void create () {
        mShapeRenderer = new ShapeRenderer();
        mCamera = new OrthographicCamera();
        mViewPort = new ExtendViewport(Constants.VIEW_SIZE_X, Constants.VIEW_SIZE_Y, mCamera);
        mCurrentViewCord = new Vector2(Constants.VIEW_SIZE_X / 2, Constants.VIEW_SIZE_Y / 2);
        moveAndUpdateCamera();

        resetGame();
    }

    @Override
    public void resize(int aWidth, int aHeight) {
        mViewPort.update(aWidth, aHeight);

    }

    private void resetGame() {
        mRoot = new TreeBranch(
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0),
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0.5f),
                true,
                1);
        mFishes.add(new Fish(0f, 0f));
        mOctopuses.add(new Octopus(1f, 10f));
        mCamera.zoom = 1.0f;
        TreeBranch.sGlobal.setZero();
        mSpeed = Constants.SPEED;
    }

    @Override
    public void render () {
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

        long after = TimeUtils.nanoTime();


        System.out.println("y:" + TreeBranch.sGlobal.y + " Time:" + (after - before) / 1000); //TODO remove

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void tick() {
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
                mSpeed *= 1.1f;
            }
        }
        removeObsticles();
    }

    /**
     * To avoid unnecessary CPU time when change the root.
     */
    private void checkForNewRoot() {
        if ( mNextRoot != null && mNextRoot != mRoot) {
            mRoot = mNextRoot;
        }
    }

    private void spawnNewStuff() {
        float ypos = TreeBranch.sGlobal.y;
        if (  (ypos * 150f/800f + 30f) * Constants.sRandom.nextFloat() < 1f ) {
            spawnFishAbove();
        }
        if ( Constants.sRandom.nextFloat() * 100f - (50f* ypos/ 1024f ) < 1f) {
            spawnOctoAbove();
        }
    }

    private void removeObsticles() {
        Iterator<Fish> fishIter = mFishes.iterator();
        while(fishIter.hasNext()) {
            if (fishIter.next().mPos.y < TreeBranch.sGlobal.y - 10f) {
                fishIter.remove();
            }
        }

        Iterator<Octopus> octoIter = mOctopuses.iterator();
        while(octoIter.hasNext()) {
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

        mCurrentViewCord.set(TreeBranch.sGlobal);
        mCamera.position.set(mCurrentViewCord, 0);
        mCamera.update();
    }
}
