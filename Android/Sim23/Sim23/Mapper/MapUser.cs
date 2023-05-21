using AutoMapper;
using Microsoft.AspNetCore.Identity;
using Sim23.Data.Entitys.Identity;
using Sim23.Models;

namespace Sim23.Mapper
{
    public class MapUser:Profile
    {
        public MapUser()
        {

            CreateMap<UserEntity, UserViewModel>()
                .ForMember(x => x.Image, opt => opt.MapFrom(x => $"/images/{x.Image}"));
        }
    }
}
