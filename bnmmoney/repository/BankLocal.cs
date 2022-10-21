using bnmmoney.module;
using bnmmoney.utilities;

namespace bnmmoney.repository
{
    public class BankLocal : IBankDecorator
    {
        private readonly FileStore fileStore;

        public BankLocal(FileStore fileStore)
        {
            this.fileStore = fileStore;
        }

        public async Task<List<Valute>> getValutes()
        {
            Console.WriteLine("The file exists.");
            var valCurs = fileStore.ReadFromXmlFile<ValCurs>(FileUtilities.getPath());
            return valCurs.Valute;
        }
    }
}
