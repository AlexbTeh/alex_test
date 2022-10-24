using AutoFixture;
using bnmmoney.module;
using bnmmoney.repository;
using bnmmoney.utilities;
using Moq;
using Moq.Protected;
using System;
using System.Net;

namespace bnmmoney_unit_tests
{
    public class BnmMoneyTests
    {
        private BankStore bankStore;
        private BankWriter bankWriter;
        private BankLocal bankLocal;

        [SetUp]
        public void Setup()
        {

            var fileStore = new Mock<IFileStore<ValCurs>>();

            var config = new  Mock<IConfigurationStore>();
            var httpClient = new Mock<IHttpClientService>();

            string date = "14.09.2022";
            var dateTime = Convert.ToDateTime(date);

            var url = string.Format(Configs.Name, dateTime);
            string path = FileUtilities.getPath();

            config.Setup(m => m.BaseUrl(dateTime)).Returns(url);
            ValCurs valCurs = new Fixture().Create<ValCurs>();

            httpClient.Setup(m => m.GetValCurs(url)).ReturnsAsync(valCurs);
            fileStore.Setup(m => m.WriteToXmlFile(path, valCurs, false));
            fileStore.Setup(m => m.ReadFromXmlFile<ValCurs>(path)).Returns(valCurs);

            this.bankStore = new BankStore(httpClient.Object, config.Object);
            this.bankWriter = new BankWriter(bankStore);
            this.bankLocal = new BankLocal(fileStore.Object, bankWriter);
        }

        [Test]
        public async Task ValutesWithWrongDateFromServer()
        {
            string date = "14.09.1960";
            var dateTime = Convert.ToDateTime(date);
            List<Valute> valutes = await bankLocal.getValutes(dateTime);

            Assert.IsTrue(valutes.Count == 0);
        }

        [Test]
        public async Task ValutesWithCorrectDateIsSuccesFromServer()
        {
            string date = "14.09.2022";
            var dateTime = Convert.ToDateTime(date);
            List<Valute> valutes = await bankLocal.getValutes(dateTime);

            Assert.IsTrue(valutes.Count > 0);
        }


        [Test]
        public void ReadFromFileSuccess()
        {
            Assert.IsTrue(bankLocal.ReadFromFile().Count > 0);
        }
    }
}