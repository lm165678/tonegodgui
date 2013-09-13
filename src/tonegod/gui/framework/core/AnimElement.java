/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.core;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import tonegod.gui.framework.animation.TemporalAction;

/**
 *
 * @author t0neg0d
 */
public abstract class AnimElement extends Node {
	Map<String, QuadData> quads = new LinkedHashMap();
	Texture tex;
	Map<String, TextureRegion> uvs = new HashMap();
	AnimElementMesh mesh;
	Vector2f origin = new Vector2f();
	float rotation;
	Vector2f position = new Vector2f();
	Vector2f scale = new Vector2f();
	Spatial spatial;
	Material mat;
	AssetManager am;
	
	public AnimElement(AssetManager am) {
		this.am = am;
		mesh = new AnimElementMesh(this);
	}
	
	public void initialize() {
		mesh.initialize();
		
		mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setTexture("ColorMap", tex);
	//	mat.setColor("Color", ColorRGBA.White);
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		
		Geometry geom = new Geometry();
		geom.setMesh(mesh);
		
		attachChild(geom);
		setMaterial(mat);
	}
	
	public void setTexture(Texture tex) {
		this.tex = tex;
	}
	
	public Texture getTexture() {
		return tex;
	}
	
	public void addTextureRegion(String regionKey, int x, int y, int w, int h) {
		TextureRegion tr = new TextureRegion(tex, x, y, w, h);
		tr.flip(false, true);
		uvs.put(regionKey, tr);
	}
	
	public TextureRegion getTextureRegion(String regionKey) {
		return uvs.get(regionKey);
	}
	
	public void addQuad(String quadKey, String regionKey, Vector2f position, Vector2f origin) {
		QuadData qd = new QuadData(quadKey, uvs.get(regionKey), position.x, position.y, uvs.get(regionKey).getRegionWidth(), uvs.get(regionKey).getRegionHeight(), origin);
		quads.put(quadKey, qd);
	}
	
	public void addQuad(String quadKey, String regionKey, Vector2f position, Vector2f origin, String parentKey) {
		QuadData qd = new QuadData(quadKey, uvs.get(regionKey), position.x, position.y, uvs.get(regionKey).getRegionWidth(), uvs.get(regionKey).getRegionHeight(), origin);
	//	qd.z = currentZOrder;
	//	currentZOrder -= zOrderInc;
		qd.parent = quads.get(parentKey);
		qd.x -= qd.parent.x;
		qd.y -= qd.parent.y;
		quads.put(quadKey, qd);
	}
	
	public void rotateQuad(String quadKey, int rotation) {
		quads.get(quadKey).rotation = rotation;
	}
	
	public void moveQuad(String quadKey, float x, float y) {
		QuadData q = quads.get(quadKey);
		q.x = x;
		q.y = y;
	}
	
	public void moveQuad(String quadKey, float z) {
		QuadData q = quads.get(quadKey);
		q.z = z;
	}
	
	public void scaleQuad(String quadKey, float scaleX, float scaleY) {
		QuadData q = quads.get(quadKey);
		q.scaleX = scaleX;
		q.scaleY = scaleY;
	}
	
	public void addQuadAction(String quadKey, TemporalAction action) {
		quads.get(quadKey).addAction(action);
	}
	
	public Map<String, QuadData> getQuads() {
		return this.quads;
	}
	
	public Map<String, TextureRegion> getUVs() {
		return this.uvs;
	}
	
	public void update(float tpf) {
		mesh.update(tpf);
		animElementUpdate(tpf);
	}
	
	public abstract void animElementUpdate(float tpf);
	
	public void setOrigin(float x, float y) {
		this.origin.set(x,y);
	}
	public void setOrigin(Vector2f origin) {
		this.origin.set(origin);
	}
	public void setOriginX(float x) { this.origin.setX(x); }
	public void setOriginY(float y) { this.origin.setY(y); }
	public Vector2f getOrigin() { return this.origin; }
	public float getOriginX() { return this.origin.x; }
	public float getOriginY() { return this.origin.y; }
	
	public void setPosition(float x, float y) {
		this.position.set(x,y);
	}
	public void setPosition(Vector2f position) {
		this.position.set(position);
	}
	public void setPositionX(float x) { this.position.setX(x); }
	public void setPositionY(float y) { this.position.setY(y); }
	public Vector2f getPosition() { return this.position; }
	public float getPositionX() { return this.position.x; }
	public float getPositionY() { return this.position.y; }
	
	public void setScale(float x, float y) {
		this.scale.set(x,y);
	}
	public void setScale(Vector2f scale) {
		this.scale.set(scale);
	}
	public void setScaleX(float x) { this.scale.setX(x); }
	public void setScaleY(float y) { this.scale.setY(y); }
	public Vector2f getScale() { return this.scale; }
	public float getScaleX() { return this.scale.x; }
	public float getScaleY() { return this.scale.y; }
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	public float getRotation() { return this.rotation; }
	
}