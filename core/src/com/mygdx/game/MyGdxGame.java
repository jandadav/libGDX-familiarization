package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.mygdx.game.instance.WireframeInstance;



public class MyGdxGame extends ApplicationAdapter {
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
    public Model model;
	public Model grid;
	public ModelInstance instance;
	public ModelInstance camTarget;
	public List<ModelInstance> instanceList = new ArrayList<>();
    public ModelInstance gridInstance;

	@Override
	public void create() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		modelBatch = new ModelBatch();
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 7f);
		cam.lookAt(0,0,0);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();
		
        ModelBuilder modelBuilder = new ModelBuilder();
		
		model = modelBuilder.createBox(1f, 1f, 1f, 
            new Material(ColorAttribute.createDiffuse(Color.YELLOW)),
			Usage.Position | Usage.Normal);
        camTarget = new WireframeInstance(model);
		instanceList.add(camTarget);

		modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.RED);
        builder.line(0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 5.0f);
		instance = new ModelInstance(modelBuilder.end());
		instanceList.add(instance);

        grid = modelBuilder.createLineGrid(5, 5, 10, 10, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Position | Usage.Normal);
        gridInstance = new ModelInstance(grid);
		instanceList.add(gridInstance);

		

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
	}

	@Override
	public void render() {
		camController.update();

		camTarget.transform.setToTranslation(camController.target);
		
		
		
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 
		modelBatch.begin(cam);
        modelBatch.render(instanceList, environment);
        modelBatch.end();
	}
	
	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
		grid.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
