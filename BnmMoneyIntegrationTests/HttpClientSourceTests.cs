using bnmmoney.module;
using bnmmoney.repository;
using bnmmoney.utilities;
using System.Net.Mail;
using System.Net;

namespace BnmMoneyIntegrationTests;

public class HttpClientSourceTests
{
    FileStore fileStore;
    ConfigurationStore config;
    HttpClientSource httpClientSource;
    BankStore bankStore;
    BankWriter bankWriter;
    BankLocal bankLocal;


    [SetUp]
    public void Setup()
    {
        this.fileStore = new FileStore();
        this.config = new ConfigurationStore();
        this.httpClientSource = new HttpClientSource();
        this.bankStore = new BankStore(httpClientSource, config);
        this.bankWriter = new BankWriter(bankStore);
        this.bankLocal = new BankLocal(fileStore, bankWriter);
    }


    [Test]
    public async Task ValutesWithCorrectDateIsNotEmpty()
    {
        string date = "14.09.2020";
        var dateTime = Convert.ToDateTime(date);

        List<Valute> valutes =  await bankLocal.getValutes(dateTime);
        Assert.True(valutes.Count > 0);
    }

    [Test]
    public async Task ValutesWithWrongDateFromServer()
    {
        string date = "14.09.1960";
        var dateTime = Convert.ToDateTime(date);

        try
        {
            var valutes = await bankWriter.getValutes(dateTime);
        }
        //assert
        catch (HttpRequestException ex)
        {
            // HttpException is expected
            Assert.That(ex.StatusCode, Is.EqualTo(HttpStatusCode.NotFound));
        }
        catch (Exception)
        {
            Assert.Fail();
        }
    }

    [Test]
    public void WriteToFileSuccess()
    {
        string path = FileUtilities.getPath();
        ValCurs valCurs = new ValCurs();
        var valutes = new List<Valute>();
        var valute = new Valute();
        valute.Name = "Euro";
        valutes.Add(valute);
        valutes.Add(valute);
        valutes.Add(valute);
        valutes.Add(valute);
        valutes.Add(valute);
        valCurs.Valute = valutes;
        fileStore.WriteToXmlFile(path, valCurs, false);

        Assert.IsTrue(File.Exists(path));
    }

    [Test]
    public void ReadFromFileSuccess()
    {
        WriteToFileSuccess();
        string path = FileUtilities.getPath();
        Assert.IsTrue(File.Exists(path), "File exists");
        var valutes = fileStore.ReadFromXmlFile<ValCurs>(path).Valute;
        Assert.IsTrue(valutes.Count > 0, "Read From File success and have items");


    }
}