using AutoFixture;
using bnmmoney;
using bnmmoney.module;
using bnmmoney.repository;
using bnmmoney.utilities;
using Moq;
using Moq.Protected;
using NUnit.Framework.Constraints;
using System;
using System.ComponentModel.DataAnnotations;
using System.IO;
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

            var IfileStore = new Mock<IFileStore<ValCurs>>();

            var config = new  Mock<IConfigurationStore>();
            var httpClient = new Mock<IHttpClientService>();

            string date = "14.09.2020";
            var dateTime = DateTime.Parse(date);

            var url = string.Format(Configs.Name, dateTime);
            string path = FileUtilities.getPath();

            config.Setup(m => m.BaseUrl(dateTime)).Returns(url);
            ValCurs valCurs = new Fixture().Create<ValCurs>();

            httpClient.Setup(m => m.GetValCurs(url)).ReturnsAsync(valCurs);
            IfileStore.Setup(m => m.WriteToXmlFile(path, valCurs, false));
            IfileStore.Setup(m => m.ReadFromXmlFile<ValCurs>(path)).Returns(valCurs);

            this.bankStore = new BankStore(httpClient.Object, config.Object);
            this.bankWriter = new BankWriter(bankStore);
            this.bankLocal = new BankLocal(IfileStore.Object, bankWriter);
        }

        [Test]
        public async Task ValutesWithWrongDateFromMockServer()
        {
            string date = "14.09.1960";
            var dateTime = DateTime.Parse(date);
            List<Valute> valutes = await bankLocal.getValutes(dateTime);

            Assert.IsTrue(valutes.Count == 0);
        }

        [Test]
        public async Task ValutesWithCorrectDateIsSuccesFromMockServer()
        {
            string date = "14.09.2020";
            var dateTime = DateTime.Parse(date);
            List<Valute> valutes = await bankLocal.getValutes(dateTime);

            Assert.IsTrue(valutes.Count > 0);
        }


        [Test]
        public void WriteFileMockSuccess()
        {
            ValCurs valCurs = new Fixture().Create<ValCurs>();
            bankLocal.WriteToFile(valCurs);
            var hasFileItems = bankLocal.ReadFromFile().Count > 0;
            Assert.IsTrue(hasFileItems);
        }


        [Test]
        public void ReadFromFileMockSuccess()
        {
            var hasFileItems = bankLocal.ReadFromFile().Count > 0;
            Assert.IsTrue(hasFileItems);
        }
    }
}