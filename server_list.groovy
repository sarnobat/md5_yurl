import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.io.Files;

public class Md5RoList {

	private static final String QUEUE_DIR = "/home/sarnobat/sarnobat.git/db/md5ro/";
	private static final String QUEUE_FILE_TXT_DELETE = QUEUE_DIR + "/md5_files.txt";

	// This only gets invoked when it receives the first request
	// Multiple instances get created
	@Path("md5ro")
	public static class YurlResource { // Must be public

		@GET
		@Path("uncategorized")
		@Produces("application/json")
		public Response getUrls(@QueryParam("rootId") Integer iRootId)
				throws JSONException, IOException {
			checkNotNull(iRootId);

			try {
				JSONObject retVal1 = new JSONObject();


				return Response.ok().header("Access-Control-Allow-Origin", "*")
						.entity(retVal1.toString()).type("application/json")
						.build();
			} catch (Exception e) {
				e.printStackTrace();
				return Response.serverError()
						.header("Access-Control-Allow-Origin", "*")
						.entity(e.getStackTrace()).type("application/text")
						.build();
			}
		}
	}

	public static void main(String[] args) throws URISyntaxException,
			JSONException, IOException {

		// Turn off that stupid Jersey logger.
		// This works in Java but not in Groovy.
		// java.util.Logger.getLogger("org.glassfish.jersey").setLevel(java.util.Level.SEVERE);
		try {
			List lines = Files
					.readLines(Paths.get(QUEUE_FILE_TXT_DELETE).toFile(),
							Charset.defaultCharset()).stream()
							.filter(new Predicate<String> (){
								public boolean test(String t) {
									return t.contains("2016");
								}})
							.limit(6)
							.collect(Collectors.toList());
			System.out.println("Md5RoList.main() " + lines);
			JdkHttpServerFactory.createHttpServer(new URI(
					"http://localhost:4490/"), new ResourceConfig(
					YurlResource.class));
			// Do not allow this in multiple processes otherwise your hard disk
			// will fill up
			// or overload the database
			// Problem - this won't get executed until the server ends
			// YurlWorldResource.downloadUndownloadedVideosInSeparateThread() ;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Not creating server instance");
		}
	}
}
