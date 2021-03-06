package com.webcheckers.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.webcheckers.application.*;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.common.net.MediaType;

import spark.Response;
import spark.Route;
/**
 * The UI Controller to GET the game page.
 *
 * @author David Authur Cole
 */
public class GetFavIconRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetFavIconRoute.class.getName());
    private static final File FAVICON_PATH = new File("src/main/resources/public/img/favicon.ico");

    public GetFavIconRoute() {
        if (WebServer.DEBUG_FLAG) LOG.config("GetFavIconRoute is initialized.");
    }

    /**
     * Render the fav icon.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the fav icon
     */
    @Override
    public Object handle(spark.Request request, Response response) throws Exception {
        try {
            InputStream in = null;
            OutputStream out;
            try {
                // copy input stream to output stream
                in = new BufferedInputStream(new FileInputStream(FAVICON_PATH));
                out = new BufferedOutputStream(response.raw().getOutputStream());
                response.raw().setContentType(MediaType.ICO.toString());
                response.status(200);
                ByteStreams.copy(in, out);
                out.flush();
                return "";
            } finally {
                // close all streams
                Closeables.close(in, true);
            }
        } catch (FileNotFoundException ex) {
            response.status(404);
            return ex.getMessage();
        } catch (IOException ex) {
            response.status(500);
            return ex.getMessage();
        }
    }

}
