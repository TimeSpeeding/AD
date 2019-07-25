using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace Group13SSIS.Models
{
    [MetadataType(typeof(UserValidation))]
    public partial class User
    {

    }

    public class UserValidation
    {
        public int UserId { get; set; }
        [Display(Name = "Username:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter username!")]
        public string Username { get; set; }
        [Display(Name = "Password:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter password!")]
        [DataType(DataType.Password)]
        [MinLength(5, ErrorMessage = "Password require a min length of 5 characters!")]
        public string Password { get; set; }
        [Display(Name = "Name:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter name!")]
        public string Name { get; set; }
        [Display(Name = "Email:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter email!")]
        [EmailAddress]
        public string Email { get; set; }
        [Display(Name = "Department:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please select a department!")]
        public int DeptId { get; set; }
    }
}