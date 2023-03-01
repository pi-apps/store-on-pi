package com.api.service.location;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;
import com.vividsolutions.jts.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 地理空间转化工具
 * 数据库存储方式必须为  [经度,纬度 (y,x)存储方式]
 */
public class GeoPointConverter {
    /**
     * Little endian or Big endian
     */
    private int byteOrder = ByteOrderValues.LITTLE_ENDIAN;
    /**
     * Precision model
     */
    private PrecisionModel precisionModel = new PrecisionModel();
    /**
     * Coordinate sequence factory
     */
    private CoordinateSequenceFactory coordinateSequenceFactory = CoordinateArraySequenceFactory.instance();
    /**
     * Output dimension
     */
    private int outputDimension = 2;

    /**
     * Convert byte array containing SRID + WKB Geometry into Geometry object
     */
    public GeoPoint from(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            // Read SRID
            byte[] sridBytes = new byte[4];
            inputStream.read(sridBytes);
            int srid = ByteOrderValues.getInt(sridBytes, byteOrder);

            // Prepare Geometry factory
            GeometryFactory geometryFactory = new GeometryFactory(precisionModel, srid, coordinateSequenceFactory);

            // Read Geometry
            WKBReader wkbReader = new WKBReader(geometryFactory);
            Geometry geometry = wkbReader.read(new InputStreamInStream(inputStream));
            Point point = (Point) geometry;
            // convert to GeoPoint  把x当成经度,y当成纬度
            GeoPoint geoPoint = new GeoPoint(point.getX(), point.getY());
            return geoPoint;
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Convert Geometry object into byte array containing SRID + WKB Geometry
     */
    public byte[] to(GeoPoint geoPoint) {
        if (geoPoint == null) {
            return null;
        }
        //获取时候,把经度当成x,纬度当成y
        Coordinate coordinate = new Coordinate(geoPoint.getLongitude(),geoPoint.getLatitude());
        CoordinateArraySequence coordinateArraySequence = new CoordinateArraySequence(new Coordinate[]{coordinate}, 2);
        Point point = new Point(coordinateArraySequence, new GeometryFactory());
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Write SRID
            byte[] sridBytes = new byte[4];
            ByteOrderValues.putInt(point.getSRID(), sridBytes, byteOrder);
            outputStream.write(sridBytes);
            // Write Geometry
            WKBWriter wkbWriter = new WKBWriter(outputDimension, byteOrder);
            wkbWriter.write(point, new OutputStreamOutStream(outputStream));
            return outputStream.toByteArray();
        } catch (IOException ioe) {
            throw new IllegalArgumentException(ioe);
        }
    }

}
