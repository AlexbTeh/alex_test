using bnmmoney.repository;

namespace bnmmoney_unit_tests
{
    public class BnmMoneyTests
    {
        private  FileStore fileStore;
        private  ConfigurationStore config;
        private  HttpClientSource httpClientSource;
        private  BankStore bankStore;
        private  BankWriter bankWriter;
        private  BankLocal bankLocal;

        [SetUp]
        public void Setup()
        {
            this.fileStore = new FileStore();
            this.config = new ConfigurationStore();
            this.httpClientSource = new HttpClientSource();
            this.bankStore = new BankStore(
                    new HttpClientSource(),
               config);
            this.bankWriter = new BankWriter(bankStore);
            this.bankLocal = new BankLocal(fileStore, bankWriter);
        }

        [Test]
        public void ValutesWithCorrectDateIsListEmpty()
        {
            string date = "14.09.2020";
            var dateTime = Convert.ToDateTime(date);
            var valutes = bankLocal.getValutes(dateTime);

            Assert.IsNotEmpty("Valutes", "Valutes is not empty", valutes.Result);
        }

        [Test]
        public void ValutesWithWrongDateFromServer()
        {
            string date = "14.09.1960";
            var dateTime = Convert.ToDateTime(date);
            var valutes = bankWriter.getValutes(dateTime);

            Assert.IsTrue(valutes.IsFaulted);
        }

        [Test]
        public void ValutesWithCorrectDateIsSuccesFromServer()
        {
            string date = "14.09.2020";
            var dateTime = Convert.ToDateTime(date);
            var valutes = bankWriter.getValutes(dateTime);

            Assert.IsTrue(valutes.IsCompletedSuccessfully);
        }
    }
}