import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to convert an XML RSS (version 2.0) feed from a given URL into the
 * corresponding HTML output file.
 *
 * @author Isam Hanif
 *
 */
public final class RSSReader {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private RSSReader() {
    }

    /**
     * Outputs the "opening" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * <html> <head> <title>the channel tag title as the page title</title>
     * </head> <body>
     * <h1>the page title inside a link to the <channel> link</h1>
     * <p>
     * the channel description
     * </p>
     * <table border="1">
     * <tr>
     * <th>Date</th>
     * <th>Source</th>
     * <th>News</th>
     * </tr>
     *
     * @param channel
     *            the channel element XMLTree
     * @param out
     *            the output stream
     * @updates out.content
     * @requires [the root of channel is a <channel> tag] and out.is_open
     * @ensures out.content = #out.content * [the HTML "opening" tags]
     */
    private static void outputHeader(XMLTree channel, SimpleWriter out) {
        assert channel != null : "Violation of: channel is not null";
        assert out != null : "Violation of: out is not null";
        assert channel.isTag() && channel.label().equals("channel") : ""
                + "Violation of: the label root of channel is a <channel> tag";
        assert out.isOpen() : "Violation of: out.is_open";

        int title = getChildElement(channel, "title");
        out.print("<html> <head> <title>");

        if (channel.child(title).numberOfChildren() > 0) {
            out.print(channel.child(title).child(0));
        } else {
            out.print("Empty Title");
        }

        out.println("</title> </head> <body>");

        int link = getChildElement(channel, "link");
        String actualLink = channel.child(link).child(0).label();
        out.print("<h1>" + "<a href=" + actualLink + ">");

        if (channel.child(title).numberOfChildren() > 0) {
            out.println(channel.child(title).child(0).label() + "</a></h1>");
        } else {
            out.println("Empty Title</a></h1>");
        }

        int description = getChildElement(channel, "description");
        out.println("<p>");
        if (channel.child(description).numberOfChildren() > 0) {
            out.println(channel.child(description).child(0).label());
        } else {
            out.println("No description");
        }

        out.println("</p>");
        out.println("<table border='1'>");
        out.println("<tr>");
        out.println("<th>Date</th>");
        out.println("<th>Source</th>");
        out.println("<th>News</th>");
        out.println("</tr>");

    }

    /**
     * Outputs the "closing" tags in the generated HTML file. These are the
     * expected elements generated by this method:
     *
     * </table>
     * </body> </html>
     *
     * @param out
     *            the output stream
     * @updates out.contents
     * @requires out.is_open
     * @ensures out.content = #out.content * [the HTML "closing" tags]
     */
    private static void outputFooter(SimpleWriter out) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        out.println("</table>");
        out.print("</body> </html>");

    }

    /**
     * Finds the first occurrence of the given tag among the children of the
     * given {@code XMLTree} and return its index; returns -1 if not found.
     *
     * @param xml
     *            the {@code XMLTree} to search
     * @param tag
     *            the tag to look for
     * @return the index of the first child of type tag of the {@code XMLTree}
     *         or -1 if not found
     * @requires [the label of the root of xml is a tag]
     * @ensures <pre>
     * getChildElement =
     *  [the index of the first child of type tag of the {@code XMLTree} or
     *   -1 if not found]
     * </pre>
     */
    private static int getChildElement(XMLTree xml, String tag) {
        assert xml != null : "Violation of: xml is not null";
        assert tag != null : "Violation of: tag is not null";
        assert xml.isTag() : "Violation of: the label root of xml is a tag";
        int indexOfChild = -1;

        for (int i = 0; i < xml.numberOfChildren(); i++) {
            if (xml.child(i).label().equals(tag)) {
                indexOfChild = i;
            }

        }

        return indexOfChild;

    }

    /**
     * Processes one news item and outputs one table row. The row contains three
     * elements: the publication date, the source, and the title (or
     * description) of the item.
     *
     * @param item
     *            the news item
     * @param out
     *            the output stream
     * @updates out.content
     * @requires [the label of the root of item is an <item> tag] and
     *           out.is_open
     * @ensures <pre>
     * out.content = #out.content *
     *   [an HTML table row with publication date, source, and title of news item]
     * </pre>
     */
    private static void processItem(XMLTree item, SimpleWriter out) {
        assert item != null : "Violation of: item is not null";
        assert out != null : "Violation of: out is not null";
        assert item.isTag() && item.label().equals("item") : ""
                + "Violation of: the label root of item is an <item> tag";
        assert out.isOpen() : "Violation of: out.is_open";

        out.println("<tr>");

        int title = getChildElement(item, "title");
        int link = getChildElement(item, "link");
        int description = getChildElement(item, "description");
        int pubDate = getChildElement(item, "pubDate");
        int source = getChildElement(item, "source");

        if (pubDate != 1) {
            out.println(
                    "<td>" + item.child(pubDate).child(0).label() + "</td>");
        } else {
            out.println("<td>No date available</td>");
        }

        if (source != 1) {
            out.println("<td><a href=\""
                    + item.child(source).attributeValue("url") + "\">"
                    + item.child(source).child(0).label() + "</a></td>");
        } else {
            out.println("<td>No source available</td>");
        }

        out.print("<td>");

        if (link != -1) {
            out.print("<a href=\"" + item.child(link).child(0).label() + "\">");

        }

        if (title != -1) {
            if (item.child(title).child(0).label().equals("")) {
                out.print("No title available");
            } else {
                out.print(item.child(title).child(0).label());
            }
        } else if (description != -1) {
            if (item.child(description).child(0).label().equals("")) {
                out.print("No description available");
            } else {
                out.print(item.child(description).child(0).label());
            }

        }

        if (link != 1) {
            out.println("</a>");
        }

        out.println("</td>");
        out.println("</tr>");

        out.println("</tr>");

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        SimpleWriter outFile = new SimpleWriter1L("RSSReader.html");

        out.print("Please enter a url for the RSS: ");
        String url = in.nextLine();

        XMLTree xml = new XMLTree1(url);

        if (xml.attributeValue("version").equals("2.0")
                && xml.label().equals("rss")) {
            int channelElement = getChildElement(xml, "channel");
            XMLTree channel = xml.child(channelElement);

            outputHeader(channel, outFile);

            for (int i = 0; i < channel.numberOfChildren(); i++) {
                if (channel.child(i).label().equals("item")) {
                    processItem(channel.child(i), outFile);
                }

            }

            outputFooter(outFile);

        }

        in.close();
        out.close();
    }

}
