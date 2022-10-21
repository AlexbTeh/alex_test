using bnmmoney.module;
using bnmmoney.utilities;

namespace bnmmoney.repository
{
    public class BankLocal : IBankDecorator
    {
        private readonly FileStore fileStore;
        private readonly IBankDecorator bankDecorator;

        public BankLocal(FileStore fileStore, IBankDecorator bankDecorator)
        {
            this.fileStore = fileStore;
            this.bankDecorator = bankDecorator;
        }

        public async Task<List<Valute>> getValutes(DateTime dateTime)
        {
            var fileCreationTime = FileUtilities.getFileCreationTime().Date;
            var isExitsAndToday = dateTime.Date == fileCreationTime;
            if (!isExitsAndToday)
            {
                List<Valute> reposnse = await bankDecorator.getValutes(dateTime);
                ValCurs valCurs = new ValCurs();
                valCurs.Valute = reposnse;
                fileStore.WriteToXmlFile(FileUtilities.getPath(), valCurs, false);
                return reposnse;
            }

            Console.WriteLine("The file exists.");
            return fileStore.ReadFromXmlFile<ValCurs>(FileUtilities.getPath()).Valute;
        }
    }
}