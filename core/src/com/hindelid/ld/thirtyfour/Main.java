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

import sun.reflect.generics.tree.Tree;

public class Main extends ApplicationAdapter {
    private Viewport mViewPort;
    private OrthographicCamera mCamera;
    private ShapeRenderer mShapeRenderer;

    private TreeBranch mRoot;
    public TreeBranch mNextRoot;
    private Vector2 mCurrentViewCord;
    private Fish mFish;

    private float mSpeed = Constants.SPEED;

    @Override
    public void create () {
        mShapeRenderer = new ShapeRenderer();
        mCamera = new OrthographicCamera();
        mViewPort = new ExtendViewport(Constants.VIEW_SIZE_X, Constants.VIEW_SIZE_Y, mCamera);
        mCurrentViewCord = new Vector2(Constants.VIEW_SIZE_X / 2, Constants.VIEW_SIZE_Y / 2);
        moveAndUpdateCamera();

        mRoot = new TreeBranch(
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0),
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0.5f),
                true,
                1);
        mFish = new Fish(1f, 10f);
    }

    @Override
    public void resize(int aWidth, int aHeight) {
        mViewPort.update(aWidth, aHeight);

    }

    @Override
    public void render () {
        long before = TimeUtils.nanoTime();
        tick();
        if (TreeBranch.sGlobal.y < 255f) {
            Gdx.gl.glClearColor(0, 0, TreeBranch.sGlobal.y / 255f, 1);
        } else if (TreeBranch.sGlobal.y < 512f) {
            Gdx.gl.glClearColor(0, (TreeBranch.sGlobal.y-256f) / 255f, 0, 1);
        } else if (TreeBranch.sGlobal.y < 768f) {
            Gdx.gl.glClearColor((TreeBranch.sGlobal.y-512f) / 255f, 0, 0, 1);
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
        mFish.render(mShapeRenderer);
        mShapeRenderer.end();

        long after = TimeUtils.nanoTime();


        System.out.println("y:" + TreeBranch.sGlobal.y + " Time:" + (after - before) / 1000); //TODO remove

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void tick() {
        moveAndUpdateCamera();
        if (TreeBranch.sNext) {
            TreeBranch.sNext = false;
            mRoot.split();
        }
        if (mRoot.checkCollision(mFish.mBoundingBox)) {
            System.out.println("Collision");
            spawnFishAbove();
            mSpeed *=1.1f;
        }
    }

    private void spawnFishAbove() {
        mFish.setPos(Constants.sRandom.nextFloat()*10f - 5f + TreeBranch.sGlobal.x, TreeBranch.sGlobal.y + 10f );
    }

    private void moveAndUpdateCamera() {
        TreeBranch.sGlobal.y += mSpeed;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            mCamera.zoom *= 5f;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            spawnFishAbove();
        }

        mCurrentViewCord.set(TreeBranch.sGlobal);
        mCamera.position.set(mCurrentViewCord, 0);
        mCamera.update();
    }
}
