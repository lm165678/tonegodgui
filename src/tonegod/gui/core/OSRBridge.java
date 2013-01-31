/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core;

import com.jme3.input.ChaseCamera;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

/**
 *
 * @author t0neg0d
 */
public class OSRBridge extends AbstractControl {
	private RenderManager rm;
	private ChaseCamera chaseCam;
	private Camera cam;
	private ViewPort vp;
	private Node root;
	private Texture2D tex;
	private float tpf = 0.01f;
	
	public OSRBridge(RenderManager rm, int width, int height, Node root) {
		this.rm = rm;
		this.root = root;

		cam = new Camera(width, height);
		
		vp = rm.createPreView("Offscreen View", cam);
		vp.setClearFlags(true, true, true);
		
		FrameBuffer offBuffer = new FrameBuffer(width, height, 1);
		
		
		cam.setFrustumPerspective(45f, 1f, 1f, 1000f);
		cam.setLocation(new Vector3f(0f, 0f, -3.75f));
		cam.lookAt(root.getLocalTranslation(), Vector3f.UNIT_Y);

		tex = new Texture2D(512, 512, Image.Format.RGBA8);
		tex.setMinFilter(Texture.MinFilter.Trilinear);
		tex.setMagFilter(Texture.MagFilter.Bilinear);

		offBuffer.setDepthBuffer(Image.Format.Depth);
		offBuffer.setColorTexture(tex);

		vp.setOutputFrameBuffer(offBuffer);
		
		setSpatial(root);
		vp.attachScene(root);
		
		chaseCam = new ChaseCamera(cam, root) {
			@Override
			 public void setDragToRotate(boolean dragToRotate) {
				this.dragToRotate = dragToRotate;
				this.canRotate = !dragToRotate;
			}
		};
		chaseCam.setDefaultDistance(5f);
		chaseCam.setMaxDistance(340f);
		chaseCam.setDefaultHorizontalRotation(0f);
		chaseCam.setDefaultVerticalRotation(0f);
		chaseCam.setZoomSensitivity(0.05f);
		cam.setFrustumFar(36000f);
		float aspect = (float)cam.getWidth() / (float)cam.getHeight();
		cam.setFrustumPerspective( 45f, aspect, 0.1f, cam.getFrustumFar() );
		chaseCam.setUpVector(Vector3f.UNIT_Y);
		chaseCam.setMinDistance(.05f);
	}
	
	public Texture2D getTexture() {
		return this.tex;
	}
	
	public ViewPort getViewPort() {
		return this.vp;
	}
	
	public Camera getCamera() {
		return this.cam;
	}
	
	public ChaseCamera getChaseCamera() {
		return this.chaseCam;
	}
	
	public RenderManager getRenderManager() {
		return this.rm;
	}
	
	public Node getRootNode() {
		return this.root;
	}
	
	public void setViewPortColor(ColorRGBA color) {
		vp.setBackgroundColor(color);
	}
	
	public float getCurrentTPF() {
		return tpf;
	}
	
//	@Override
//	public void update(float tpf) {
//		this.tpf = tpf;
//	}
	
	@Override
	public final void setSpatial(Spatial spatial) {
		this.spatial = spatial;
		this.setEnabled(true);
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		root.updateLogicalState(tpf);
		root.updateGeometricState();
		
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		
	}
	
}
