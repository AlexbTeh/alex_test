

namespace bnmmoney.utilities
{
    public static class FileUtilities
    {

        public static string getPath()
        {
            return Path.GetFullPath(AppDomain.CurrentDomain.BaseDirectory) + "tmpbnm.xml";
        }

        public static DateTime getFileCreationTime()
        {
            FileInfo file = new FileInfo(getPath());
            return file.LastWriteTime;
        }
    }
}
