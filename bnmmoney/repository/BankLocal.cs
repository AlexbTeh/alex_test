using bnmmoney.module;
using bnmmoney.utilities;

namespace bnmmoney.repository
{
    public class BankLocal : IBankDecorator
    {
        private readonly IFileStore<ValCurs> fileStore;
        private readonly IBankDecorator bankDecorator;

        public BankLocal(IFileStore<ValCurs> fileStore, IBankDecorator bankDecorator)
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
                WriteToFile(valCurs);
                return reposnse;
            }

            Console.WriteLine("The file exists.");
            return ReadFromFile();
        }

        public void WriteToFile(ValCurs valCurs)
        {
            fileStore.WriteToXmlFile(FileUtilities.getPath(), valCurs, false);
        }

        public List<Valute> ReadFromFile()
        {
            return fileStore.ReadFromXmlFile<ValCurs>(FileUtilities.getPath()).Valute;
        }
    }
}