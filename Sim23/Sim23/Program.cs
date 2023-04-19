using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.FileProviders;
using Sim23.Data;
using Sim23.Mapper;

var builder = WebApplication.CreateBuilder(args);


//Add database context
builder.Services.AddDbContext<AppEFContext>(opt =>
{
    opt.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection"));
});


// Add services to the container.

builder.Services.AddControllers();


//Add AutoMapper
builder.Services.AddAutoMapper(typeof(AppMapProfile));


// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configure the HTTP request pipeline.
//if (app.Environment.IsDevelopment())
//{
    app.UseSwagger();
    app.UseSwaggerUI();
//}

//Відкриває доступ до статичних файлів у папці
var dir = Path.Combine(Directory.GetCurrentDirectory(),"images");
if(!Directory.Exists(dir))
    Directory.CreateDirectory(dir);

app.UseStaticFiles(new StaticFileOptions
{
    FileProvider = new PhysicalFileProvider(dir),
    RequestPath = "/images"
});

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.SeedData();

app.Run();
