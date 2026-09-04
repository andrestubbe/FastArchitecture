package screenzoom;

import screenzoom.control.ZoomController;
import screenzoom.view.ZoomOverlay;

/**
 * Application Entry Point: Assembles Model, Control, View.
 */
public final class Main {

    public static void main(String[] args) {
        System.out.println("FastArchitecture ScreenZoom Demo running...");

        ZoomController controller = new ZoomController();
        ZoomOverlay view = new ZoomOverlay();

        // Simulate user action: Toggle zoom & scroll
        controller.onToggleZoom();
        controller.onMouseMove(960, 540);
        controller.onMouseWheel(2); // Zoom in

        // Present view
        view.render(controller.getState());

        System.out.println("Demo completed successfully.");
    }
}
