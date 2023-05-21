using AutoMapper;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Sim23.Abstract;
using Sim23.Data;
using Sim23.Data.Constants;
using Sim23.Data.Entitys.Identity;
using Sim23.Helpers;
using Sim23.Models;

namespace Sim23.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly UserManager<UserEntity> _userManager;
        private readonly IJwtTokenService _jwtTokenService;
        private readonly IMapper _mapper;
        public UserController(UserManager<UserEntity> userManager, IJwtTokenService jwtTokenService, IMapper mapper)
        {
            _userManager = userManager;
            _jwtTokenService = jwtTokenService;
            _mapper = mapper;
        }


        [HttpPut("updateProfile")]
        public async Task<IActionResult> update([FromBody] UserProfileViewModel model)
        {
            var user = await _userManager.FindByEmailAsync(model.Email);
            if (user == null)
                return NotFound();
            else
            {
                user.FirstName = model.FirstName;
                user.LastName = model.LastName;

                if (!string.IsNullOrEmpty(model.ImageBase64))
                {
                    ImageWorker.RemoveImage(user.Image);
                    user.Image = ImageWorker.SaveImage(model.ImageBase64);
                }
                await _userManager.UpdateAsync(user);
            }
            var token = await _jwtTokenService.CreateToken(user);
            return Ok(new { token });
        }
    }
}
