/*
 * Copyright (c) 2018, 2021, Gluon
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.xml.parsers.SAXParser;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        MapView mapView = new MapView();
        //mapView.addLayer(positionLayer());
        mapView.addLayer(new PollutionLayer(mapView));
        mapView.setZoom(14);


        stage.setTitle("Gluon Maps Demo");

        StackPane root = new StackPane();
        root.getChildren().add(mapView);
        Scene scene = new Scene(root, 900, 600);

        stage.setScene(scene);
        stage.show();

        MapPoint startPosition = new MapPoint(51.508045, -0.128217); //coordinates for trafalgar square
        mapView.flyTo(0., startPosition, 0.1); //Spawns map instantly
    }

    private MapLayer positionLayer() {
        PoiLayer answer = new PoiLayer();
        answer.addPoint(new MapPoint(51.508045, -0.128217), new Circle(7, Color.RED));

        Circle circle = new Circle(30, Color.YELLOW); // Large radius to show
        circle.setOpacity(0.4); // Transparency to blend colors
        MapPoint pollutionPoint = new MapPoint(51.51275, -0.117278); //coordinates for bush house
        //answer.addPoint(pollutionPoint, circle);

        return answer;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}